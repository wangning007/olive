package com.inspur.crawler.cnblog;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wang.ning
 * @create 2020-02-21 19:45
 */
public class CnBlogCrawlerThread implements Runnable {

    public CnBlogCrawlerThread(ArrayBlockingQueue<String> queue){
        this.arrayBlockingQueue = queue;
    }

    private ArrayBlockingQueue<String> arrayBlockingQueue;

    public void run() {
        try {
           while(true){
               String url = arrayBlockingQueue.take();
               System.out.println("取一个URL:"+url);
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
