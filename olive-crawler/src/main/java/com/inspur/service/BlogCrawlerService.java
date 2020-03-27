package com.inspur.service;

import com.alibaba.fastjson.JSONObject;
import com.inspur.api.crawler.IBlogCrawlerService;
import com.inspur.crawler.cnblog.CrawlerThread;
import com.inspur.crawler.cnblog.HttpClientUtils;
import com.inspur.crawler.ehcache.EhCacheUtil;
import com.inspur.dao.BlogIntroMapper;
import com.inspur.enums.ResponseEnum;
import com.inspur.model.crawler.BlogIntro;
import com.inspur.utils.DocumentUtil;
import com.inspur.utils.HttpRequestUtil;
import com.inspur.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wang.ning
 * @create 2020-03-18 12:41
 */
@Service
@Slf4j
public class BlogCrawlerService implements IBlogCrawlerService {

    @Autowired
    private BlogIntroMapper blogIntroMapper;

    @Autowired
    private DocumentUtil documentUtil;


    @Override
    public void crawlerBlogs() {

    }

    @Override
    public void updateCrawlerBlogs() {
        Cache urlCache = EhCacheUtil.getCache(EhCacheUtil.URL_CACHE_NAME);
        if(urlCache == null){
            this.writeBlogsURLToEhcache();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,20,1, TimeUnit.HOURS,queue);

        for(int i = 1; i <= 200; i++){
            executor.execute(new CrawlerThread(httpClient,urlCache,i));
        }
        executor.shutdown();
    }

    class CrawlerThread implements Runnable{

        public CrawlerThread(CloseableHttpClient client,Cache urlCache,int page){
            this.client = client;
            this.urlCache = urlCache;
            this.page = page;
        }

        private CloseableHttpClient client;

        private Cache urlCache;  //URL缓存

        private int page;   //当前页数

        @Override
        public void run() {
            HttpPost httpPost= HttpRequestUtil.generateHttpPostRequest("https://www.cnblogs.com/AggSite/AggSitePostList",null);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CategoryId",106876);
            jsonObject.put("CategoryType","SiteCategory");
            jsonObject.put("ItemListActionName","AggSitePostList");
            jsonObject.put("PageIndex",page);
            jsonObject.put("ParentCategoryId",2);
            jsonObject.put("TotalPostCount",4000);

            httpPost.setEntity(HttpRequestUtil.generateStringEntiry(jsonObject));

            try {
                CloseableHttpResponse response= client.execute(httpPost);
                log.info("状态码:"+response.getStatusLine().getStatusCode());
                if(ResponseEnum.SUCCESS.getCode() == (response.getStatusLine().getStatusCode())){
                    log.info("抓取成功!");
                    //log.info(EntityUtils.toString(response.getEntity(),"utf-8"));
                    Document document = Jsoup.parse(EntityUtils.toString(response.getEntity()),"utf-8");
                    List<BlogIntro> blogIntros = documentUtil.parseCnBlogsIntro(document);
                    for(BlogIntro blogIntro:blogIntros){
                        net.sf.ehcache.Element element = urlCache.get(blogIntro.getUrl());
                        if(element == null){
                            //执行插入
                            EhCacheUtil.putCacheValue(EhCacheUtil.URL_CACHE_NAME,blogIntro.getUrl(),blogIntro.getUrl());
                            int flag = blogIntroMapper.saveBlogIntro(blogIntro);
                            log.info("insert:"+flag);
                        }else{
                            //获取简介内容比对
                            //获取详情内容比对
                        }
                    }
                }else{
                    log.error("请求异常:"+response.getStatusLine().getStatusCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void writeBlogsURLToEhcache() {
        List<BlogIntro> list = blogIntroMapper.getAllBlogIntro();
        for(BlogIntro blogIntro:list){
            EhCacheUtil.putCacheValue(EhCacheUtil.URL_CACHE_NAME,blogIntro.getUrl(),blogIntro.getUrl());
        }
        log.info("blog url write ehcache sucess!!");
    }

    /**
     * 解析一级内容
     * @param document
     */
    private void parseLevel1Content(Document document, String url) throws Exception{
        Elements elements = document.select(" .post_item_body");

        if(elements.size() > 0){

            log.info("一级数据爬取成功......进行下一步一级内容入库操作...");

            for(Element element:elements) {
                Elements titles = element.select("h3 a");

                String elementTitle = titles.get(0).text();
                String elementUrl = titles.get(0).attr("href");

                Elements summarys = element.select(".post_item_summary");
                String summary = summarys.get(0).text();

                Long intId = new SnowFlakeIdGenerator(1,2).nextId();

                //int flag = queryRunner.update(ConnectionUtil.getConnection(),sql,intId,elementTitle,summary,elementUrl);
                BlogIntro blog = new BlogIntro();
                blog.setIntId(intId);
                blog.setSummary(summary);
                blog.setUrl(elementUrl);
                blog.setTitle(elementTitle);

                int flag = blogIntroMapper.saveBlogIntro(blog);
                if(flag > 0){
                    log.info(url+":一级数据已爬取入库!!.....开始下一步：爬取二级数据.......");
                    //String content = HttpClientUtils.doGetStringMultiThread(client,elementUrl);
                    //Document document2 = Jsoup.parse(content);
                    //this.parseLevel2Content(document2,elementUrl);
                }else{
                    log.error(url+"数据爬取失败!!");
                }

            }
        }
    }


}
