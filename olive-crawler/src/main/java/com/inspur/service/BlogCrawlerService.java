package com.inspur.service;

import com.alibaba.fastjson.JSONObject;
import com.inspur.api.crawler.IBlogCrawlerService;
import com.inspur.crawler.cnblog.HttpClientUtils;
import com.inspur.crawler.ehcache.EhCacheUtil;
import com.inspur.dao.BlogIntroMapper;
import com.inspur.model.crawler.BlogIntro;
import com.inspur.utils.HttpRequestUtil;
import com.inspur.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
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

/**
 * @author wang.ning
 * @create 2020-03-18 12:41
 */
@Service
@Slf4j
public class BlogCrawlerService implements IBlogCrawlerService {

    @Autowired
    private BlogIntroMapper blogIntroMapper;


    @Override
    public void crawlerBlogs() {

    }

    @Override
    public void updateCrawlerBlogs() {
        Cache urlCache = EhCacheUtil.getCache(EhCacheUtil.URL_CACHE_NAME);
        if(urlCache == null){
            this.writeBlogsURLToEhcache();
        }




        /*HttpPost httpPost = new HttpPost("https://www.cnblogs.com/AggSite/AggSitePostList");
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
        httpPost.setHeader("Content-Type","application/json; charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CategoryId",106876);
        jsonObject.put("CategoryType","SiteCategory");
        jsonObject.put("ItemListActionName","AggSitePostList");
        jsonObject.put("PageIndex",page);
        jsonObject.put("ParentCategoryId",2);
        jsonObject.put("TotalPostCount",4000);

        StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
        httpPost.setEntity(entity);

        CloseableHttpResponse response= client.execute(httpPost);
        System.out.println("状态码:"+response.getStatusLine().getStatusCode());;

        String html = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));

        this.parseLevel1Content(Jsoup.parse(html),"222");*/


    }

    class CrawlerThread implements Runnable{

        public CrawlerThread(CloseableHttpClient client){
            this.client = client;
        }

        private CloseableHttpClient client;

        @Override
        public void run() {
            HttpPost httpPost= HttpRequestUtil.generateHttpPostRequest("https://www.cnblogs.com/AggSite/AggSitePostList",null);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CategoryId",106876);
            jsonObject.put("CategoryType","SiteCategory");
            jsonObject.put("ItemListActionName","AggSitePostList");
            //jsonObject.put("PageIndex",page);
            jsonObject.put("ParentCategoryId",2);
            jsonObject.put("TotalPostCount",4000);

            httpPost.setEntity(HttpRequestUtil.generateStringEntiry(jsonObject));

            try {
                CloseableHttpResponse response= client.execute(httpPost);
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
