package com.inspur.controller;

import com.inspur.crawler.cnblog.CrawlerThread;
import com.inspur.crawler.ehcache.EhCacheUtil;
import com.inspur.dao.BlogIntroMapper;
import com.inspur.model.crawler.BlogIntro;
import com.inspur.service.BlogCrawlerService;
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

    @Autowired
    BlogCrawlerService blogCrawlerService;

    @GetMapping("/getAllBlogs")
    public List<BlogIntro> getAllBlogIntro(){
        return blogIntroMapper.getAllBlogIntro();
    }

    @RequestMapping("start")
    public void start(){
        blogCrawlerService.updateCrawlerBlogs();
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
