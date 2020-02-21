package com.inspur.crawler.cnblog;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wang.ning
 * @create 2020-02-21 19:33
 */
public class CnBlogUrlThread implements Runnable {

    private static final Logger logger = Logger.getLogger(CnBlogUrlThread.class);

    public CnBlogUrlThread(ArrayBlockingQueue<String> queue,CloseableHttpClient client){
        this.arrayBlockingQueue = queue;
        this.client = client;
    }

    private ArrayBlockingQueue<String> arrayBlockingQueue;

    private CloseableHttpClient client;

    private String URL = "https://www.cnblogs.com/cate/java/#p";

    private int limit = 200;
    private int start = 2;


    public void run() {
        try {
            while (true){
                for(int i = start; i <= limit; i++){
                    URL += i;
                    //校验是否有内容
                    if(HttpClientUtils.validate(client,URL)){
                        arrayBlockingQueue.put(URL);
                        logger.info("存入url:"+URL+",目前URL剩余:"+arrayBlockingQueue.size());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateUrl(String url){

        return false;
    }
}
