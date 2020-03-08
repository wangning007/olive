package com.inspur.crawler;

import com.alibaba.fastjson.JSONObject;
import com.inspur.crawler.cnblog.HttpClientUtils;
import com.inspur.crawler.geo.SimpleCoodinates;
import com.inspur.crawler.geo.WgsGcjConverter;
import com.inspur.jdbc.ConnectionUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang.ning
 * @create 2020-02-19 19:27
 */
public class Test {

    private static final Logger logger = Logger.getLogger(Test.class);


    public static void main(String[] args) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(50);

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).build();
        try {
            /*HttpGet httpGet = new HttpGet("https://www.cnblogs.com/cate/java/#p6");
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
            CloseableHttpResponse response6 = client.execute(httpGet);
            //String response = HttpClientUtils.doGetString(connectionManager,"https://www.cnblogs.com/cate/java/#p6");
            String s6 = EntityUtils.toString(response6.getEntity(),"utf-8");*/

            HttpPost httpPost = new HttpPost("https://www.cnblogs.com/AggSite/AggSitePostList");
            //https://www.cnblogs.com/mvc/AggSite/PostList.aspx
            //HttpPost httpPost = new HttpPost("https://www.cnblogs.com/mvc/AggSite/PostList.aspx");
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
            //httpPost.setHeader("referer","https://www.cnblogs.com/");
            //httpPost.setHeader("origin","https://www.cnblogs.com");
            httpPost.setHeader("Content-Type","application/json; charset=UTF-8");

            /*ArrayList<BasicNameValuePair> parameters = new ArrayList();
            parameters.add(new BasicNameValuePair("CategoryId","106876"));
            parameters.add(new BasicNameValuePair("CategoryType","SiteCategory"));
            parameters.add(new BasicNameValuePair("ItemListActionName","AggSitePostList"));
            parameters.add(new BasicNameValuePair("PageIndex","6"));
            parameters.add(new BasicNameValuePair("ParentCategoryId","2"));
            parameters.add(new BasicNameValuePair("TotalPostCount","4000"));

            httpPost.setEntity(new UrlEncodedFormEntity(parameters));*/

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

            String html = EntityUtils.toString(response.getEntity(),Charset.forName("utf-8"));

            System.out.println(html);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
