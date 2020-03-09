package com.inspur.crawler.cnblog;

import com.alibaba.fastjson.JSONObject;
import com.inspur.jdbc.ConnectionUtil;
import com.inspur.jdbc.DbUtil;
import com.inspur.utils.SnowFlakeIdGenerator;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.sql.Connection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wang.ning
 * @create 2020-02-21 19:45
 */
public class CnBlogCrawlerThread implements Runnable {

    private static final Logger logger = Logger.getLogger(CnBlogCrawlerThread.class);

    public CnBlogCrawlerThread(ArrayBlockingQueue<String> queue, CloseableHttpClient client) {
        this.arrayBlockingQueue = queue;
        this.client = client;
        this.queryRunner = DbUtil.getQueryRunner();
    }

    private ArrayBlockingQueue<String> arrayBlockingQueue;
    private CloseableHttpClient client;
    private QueryRunner queryRunner;

    public void run() {
        try {

               //String url = arrayBlockingQueue.take();
               //String url = "https://www.cnblogs.com/AggSite/AggSitePostList";
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://www.cnblogs.com/AggSite/AggSitePostList");
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
            httpPost.setHeader("Content-Type","application/json; charset=UTF-8");
            for(int i = 1; i<= 200;i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CategoryId",106876);
                jsonObject.put("CategoryType","SiteCategory");
                jsonObject.put("ItemListActionName","AggSitePostList");
                jsonObject.put("PageIndex",6);
                jsonObject.put("ParentCategoryId",2);
                jsonObject.put("TotalPostCount",4000);

                StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
                httpPost.setEntity(entity);

                CloseableHttpResponse response = client.execute(httpPost);
                System.out.println("状态码:"+response.getStatusLine().getStatusCode());;

                String html = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                parseLevel1Content(Jsoup.parse(html),"1");
            }

               /*for(int i = 1; i <= 200;i++){
                   JSONObject jsonObject = new JSONObject();
                   jsonObject.put("CategoryId",106876);
                   jsonObject.put("CategoryType","SiteCategory");
                   jsonObject.put("ItemListActionName","AggSitePostList");
                   jsonObject.put("PageIndex",i);
                   jsonObject.put("ParentCategoryId",2);
                   jsonObject.put("TotalPostCount",4000);

                   logger.info("爬取第"+i+"页数据....");
                   StringEntity stringEntity = new StringEntity(jsonObject.toString(),"utf-8");
                   CloseableHttpResponse response = HttpClientUtils.handlePost(client,url,stringEntity);
                   int code = response.getStatusLine().getStatusCode();
                   if( code == 200){
                       String content = EntityUtils.toString(response.getEntity(),"utf-8");
                       System.out.println(content);
                       Document document = Jsoup.parse(content);
                       parseLevel1Content(document,url);
                   }

               }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析一级内容
     * @param document
     */
    private void parseLevel1Content(Document document,String url) throws Exception{
        Elements elements = document.select("#post_list .post_item .post_item_body");

        if(elements.size() > 0){

            logger.info("一级数据爬取成功......进行下一步一级内容入库操作...");

            for(Element element:elements) {
                Elements titles = element.select("h3 a");

                String elementTitle = titles.get(0).text();
                String elementUrl = titles.get(0).attr("href");

                Elements summarys = element.select(".post_item_summary");
                String summary = summarys.get(0).text();

                String sql = "insert into blog_intro (int_id,title,summary,url) values(?,?,?,?)";

                Long intId = new SnowFlakeIdGenerator(1,2).nextId();

                int flag = queryRunner.update(ConnectionUtil.getConnection(),sql,intId,elementTitle,elementUrl,summary);

                if(flag > 0){
                    logger.info(url+":一级数据已爬取入库!!.....开始下一步：爬取二级数据.......");
                    String content = HttpClientUtils.doGetStringMultiThread(client,elementUrl);
                    Document document2 = Jsoup.parse(content);
                    this.parseLevel2Content(document2,elementUrl);
                }else{
                    logger.error(url+"数据爬取失败!!");
                }

            }
        }
    }

    /**
     * 解析二级内容
     * @param document
     * @param url
     */
    private void parseLevel2Content(Document document,String url) throws Exception{
        Element contentElement = document.getElementById("cnblogs_post_body");
        String content = contentElement.html();

        Element titleElement = document.getElementById("cb_post_title_url");
        String contentTitle = titleElement.text();

        String contentSql = "insert into blog_detail (int_id,title,contenta) values(?,?,?)";
        Long contentId = new SnowFlakeIdGenerator(1,2).nextId();
        Object[] contentParams = {contentId,contentTitle,content.getBytes()};


        int flag = queryRunner.update(ConnectionUtil.getConnection(),contentSql,contentParams);

        if(flag > 0){
            logger.info(url+":二级级数据已爬取入库!!.......");
        }else{
            logger.error(url+"二级数据爬取失败!!");
        }
    }
}
