package com.inspur.controller;

import com.inspur.api.crawler.bean.BlogIntro;
import com.inspur.crawler.cnblog.CrawlerThread;
import com.inspur.dao.BlogIntroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @RequestMapping("test")
    public void test(){
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,20,1, TimeUnit.HOURS,queue);

        for(int i = 1; i <= 200; i++){
            executor.execute(new CrawlerThread(i,blogIntroMapper));
        }
        executor.shutdown();
    }

    @RequestMapping("/test2")
    public List<BlogIntro> test2(){
        return blogIntroMapper.getAllBlogIntro();
    }
}
