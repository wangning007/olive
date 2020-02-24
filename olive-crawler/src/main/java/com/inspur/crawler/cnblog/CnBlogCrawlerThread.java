package com.inspur.crawler.cnblog;

import com.inspur.jdbc.ConnectionUtil;
import com.inspur.jdbc.DbUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wang.ning
 * @create 2020-02-21 19:45
 */
public class CnBlogCrawlerThread implements Runnable {

    private static final Logger logger = Logger.getLogger(CnBlogCrawlerThread.class);

    public CnBlogCrawlerThread(ArrayBlockingQueue<String> queue,HttpClientConnectionManager httpClientConnectionManager) {
        this.arrayBlockingQueue = queue;
        this.httpClientConnectionManager = httpClientConnectionManager;
        this.queryRunner = DbUtil.getQueryRunner();
    }

    private ArrayBlockingQueue<String> arrayBlockingQueue;
    private HttpClientConnectionManager httpClientConnectionManager;
    private QueryRunner queryRunner;

    public void run() {
        try {
           while(true){
               String url = arrayBlockingQueue.take();
               logger.info("开始爬取:"+url);
               String content = HttpClientUtils.doGetString(httpClientConnectionManager,url);
               Document document = Jsoup.parse(content);
               parseLevel1Content(document,url);
           }
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

                int flag = queryRunner.update(ConnectionUtil.getConnection(),sql,elementTitle,elementUrl,summary);

                if(flag > 0){
                    logger.info(url+":一级数据已爬取入库!!.....开始下一步：爬取二级数据.......");
                    String content = HttpClientUtils.doGetString(httpClientConnectionManager,elementUrl);
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
    private void parseLevel2Content(Document document,String url){
        Elements elements = document.select("");
    }
}
