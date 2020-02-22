package com.inspur.crawler.ehcache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * @author wang.ning
 * @create 2020-02-22 10:07
 */
public class EhCacheUtil {

    private static Cache cache = null;

    static {
        CacheManager cacheManager = CacheManager.create(EhCacheUtil.class.getResourceAsStream("/ehcache.xml"));
        cache = cacheManager.getCache("HelloWorldCache");
    }

    public static Cache getCache(){

        return cache;
    }
}
