package com.inspur.crawler.cnblog;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wang.ning
 * @create 2020-02-21 19:42
 */
public class CnBlogTest {

    public static void main(String[] args) {

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,20,1, TimeUnit.HOURS,queue);

        for(int i = 1; i <= 200; i++){
            executor.execute(new CrawlerThread(i));
        }
        executor.shutdown();


        //创建HTtpClient链接池
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(200);
//        connectionManager.setDefaultMaxPerRoute(50);
//
//        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).build();
//
//        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(201,true);
//        //Runnable urlThread = new CnBlogUrlThread(arrayBlockingQueue,client);
//        //new Thread(urlThread).start();
//        Runnable crawler = new CnBlogCrawlerThread(arrayBlockingQueue,client);
//        new Thread(crawler).start();
    }
}
