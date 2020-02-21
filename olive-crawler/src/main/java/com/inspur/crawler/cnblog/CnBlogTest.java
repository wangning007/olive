package com.inspur.crawler.cnblog;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wang.ning
 * @create 2020-02-21 19:42
 */
public class CnBlogTest {

    public static void main(String[] args) {
        //创建HTtpClient链接池
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(50);

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).build();

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(200,true);
        Runnable runnable = new CnBlogUrlThread(arrayBlockingQueue,client);
        new Thread(runnable).start();
//        Runnable crawler = new CnBlogCrawlerThread(arrayBlockingQueue);
//        new Thread(crawler).start();
    }
}
