package com.inspur.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.Map;

/**
 * @author wang.ning
 * @create 2020-03-19 8:33
 */
public class HttpRequestUtil {

    public static StringEntity generateStringEntiry(JSONObject jsonObject){
        StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
        return entity;
    }

    /**
     * 获取post请求
     * @param url  请求url地址
     * @param headers header参数:User-Agent/Content-Type
     * @return
     */
    public static HttpPost generateHttpPostRequest(String url, Map<String,String> headers){
        HttpPost httpPost = new HttpPost(url);
        if(headers == null){
            httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
            httpPost.setHeader("Content-Type","application/json; charset=UTF-8");
        }
        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
        }
        return httpPost;
    }

    public static JSONObject generateParamsObj(JSONObject jsonObject,String key,String value){
        jsonObject.put(key,value);
        return jsonObject;
    }
}
