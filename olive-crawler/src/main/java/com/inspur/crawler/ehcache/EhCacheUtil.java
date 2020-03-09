package com.inspur.crawler.ehcache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang.ning
 * @create 2020-02-22 10:07
 */
public class EhCacheUtil {

    public static final String  MAP_CACHE_NAME = "mapCache";
    public static final String ENTITY_CACHE_NAME ="entityCache";
    public static final String COMMON_CACHE_NAME = "commonCache";
    public static final String DICTIONARY_CACHE_NAME = "dictionaryCache";
    public static final String  URL_CACHE_NAME = "urlCache";

    public static final CacheManager cacheManager = CacheManager.create();
    public static Map<String,Cache> cacheMap = new HashMap<>();

    /**
     * 根据缓存库名字获取缓存库实例
     * @param cacheName
     * @return
     */
    public static Cache getCache(String cacheName){
        if(cacheMap.get(cacheName) == null){
            cacheMap.put(cacheName,cacheManager.getCache(cacheName));
        }
        return cacheMap.get(cacheName);
    }

    /**
     * 跟key查询指定缓存库中的value
     * @param cacheName  缓存库名称
     * @param key        key值
     * @return
     */
    public static Object getCacheValue(String cacheName,Object key){
        Cache cache = cacheMap.get(cacheName);
        if(cache != null){
            Element element = cache.get(key);
            if(element != null){
                return element.getObjectValue();
            }
        }
        return null;
    }

    /**
     * 向指定的缓存库存入一个缓存
     * @param cacheName 缓存库名称
     * @param key  key值
     * @param value 缓存的值
     */
    public static void putCacheValue(String cacheName,Object key ,Object value){
        Cache cache = cacheMap.get(cacheName);
        if(cache != null){
            cache.put(new Element(key,value));
            cache.flush();
        }
    }

    /**
     * 删除缓存
     * @param cacheName
     * @param key
     * @return
     */
    public static boolean removeCacheValue(String cacheName,Object key){
        Cache cache = cacheMap.get(cacheName);
        if(cache != null){
            return cache.remove(key);
        }
        return false;
    }

    /**
     * 是否存在key
     * @param cacheName
     * @param key
     * @return
     */
    public static boolean containsKey(String cacheName,Object key){
        Cache cache = cacheMap.get(cacheName);
        Element element = cache.getQuiet(key);
        if(cache.isKeyInCache(key) && element != null && cache.isExpired(element)){
            return true;
        }
        return false;
    }

}
