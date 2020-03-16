package com.inspur.controller;

import com.inspur.api.crawler.bean.BlogIntro;
import com.inspur.crawler.cnblog.CrawlerThread;
import com.inspur.crawler.ehcache.EhCacheUtil;
import com.inspur.dao.BlogIntroMapper;
import net.sf.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wang.ning
 * @create 2020-03-11 12:36
 */
@RestController
@RequestMapping("blog")
public class BlogIntroController {

    @Autowired
    BlogIntroMapper blogIntroMapper;

    @GetMapping("/getAllBlogs")
    public List<BlogIntro> getAllBlogIntro(){
        return blogIntroMapper.getAllBlogIntro();
    }

    @RequestMapping("start")
    public void start(){
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,20,1, TimeUnit.HOURS,queue);

        for(int i = 1; i <= 200; i++){
            executor.execute(new CrawlerThread(i,blogIntroMapper));
        }
        executor.shutdown();
    }
    @GetMapping("/writeEhcache")
    public void writeToEhCache(){
        List<BlogIntro> list = blogIntroMapper.getAllBlogIntro();
        for(BlogIntro blogIntro:list){
            EhCacheUtil.putCacheValue(EhCacheUtil.URL_CACHE_NAME,blogIntro.getUrl(),blogIntro.getUrl());
        }
        System.out.println(EhCacheUtil.getCacheValue(EhCacheUtil.URL_CACHE_NAME,list.get(0).getUrl()));
    }


    @GetMapping("/getUrlCache")
    public String getValueFromUrlCache(@RequestParam String key){
        Cache cache = EhCacheUtil.getCache(EhCacheUtil.URL_CACHE_NAME);
        List list = cache.getKeys();
        for(Object obj :list){
            System.out.println(Objects.toString(obj));
        }
        return null;
    }

    @PostMapping("/getBlogIntro")
    public BlogIntro getBlogInto(@RequestBody BlogIntro blogIntro){
        return blogIntroMapper.getBlogIntro(blogIntro);
    }
}
