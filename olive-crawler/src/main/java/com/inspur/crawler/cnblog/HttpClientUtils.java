package com.inspur.crawler.cnblog;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author wang.ning
 * @create 2020-02-21 20:11
 */
public class HttpClientUtils {

    public static boolean validate(CloseableHttpClient client,String url) throws Exception{
        CloseableHttpResponse response = doGetMultiThread(client,url);
        String content = response.getStatusLine().getStatusCode() == 200 ? EntityUtils.toString(response.getEntity(),"utf-8"):"";
        return content.length() > 0 ? true:false;
    }

    public static boolean doGetAndvalidate(HttpClientConnectionManager connectionManager,String url) throws Exception{
        return doGetString(connectionManager,url).length() > 0 ? true:false;
    }

    /**
     *
     * @param connectionManager
     * @param url
     * @return
     * @throws Exception
     */
    public static String doGetString(HttpClientConnectionManager connectionManager, String url) throws Exception{
        CloseableHttpResponse response = doGet(connectionManager,url);
        return response.getStatusLine().getStatusCode() == 200 ? EntityUtils.toString(response.getEntity(),"utf-8"):"";
    }

    /**
     *
     * @param client 请求client
     * @param url
     * @return
     */
    public static String doGetStringMultiThread(CloseableHttpClient client,String url) throws Exception{
        CloseableHttpResponse response = doGetMultiThread(client,url);
        return response.getStatusLine().getStatusCode() == 200 ? EntityUtils.toString(response.getEntity(),"utf-8"):"";
    }

    /**
     * 【单线程模式下】获取请求内容response
     * @param connectionManager
     * @param url
     * @return
     */
    public static CloseableHttpResponse doGet(HttpClientConnectionManager connectionManager, String url){
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
        return handle(client,url);

    }

    /**
     * 【多线程模式下】获取请求内容
     * @param client
     * @param url
     * @return
     */
    public static CloseableHttpResponse doGetMultiThread(CloseableHttpClient client,String url){
        return handle(client,url);
    }

    public static CloseableHttpResponse handlePost(CloseableHttpClient client, String url, StringEntity stringEntity){
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)                //设置连接超时
                .setConnectionRequestTimeout(500)        //从连接池中获取到连接超时
                .setSocketTimeout(10000)                 //数据传输超时
                .build();
        post.setConfig(requestConfig);
        post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        post.setHeader("Content-Type","application/json; charset=UTF-8");
        post.setEntity(stringEntity);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static CloseableHttpResponse handle(CloseableHttpClient client,String url){
        //创建get请求
        HttpGet httpGet = new HttpGet(url);

        //构建请求配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)                //设置连接超时
                .setConnectionRequestTimeout(500)        //从连接池中获取到连接超时
                .setSocketTimeout(10000)                 //数据传输超时
                .build();

        httpGet.setConfig(requestConfig);

        //模拟浏览器访问
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");

        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpGet);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
