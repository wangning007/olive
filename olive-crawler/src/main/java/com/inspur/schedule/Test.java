package com.inspur.schedule;

import com.inspur.service.BlogCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

/**
 * @author wang.ning
 * @create 2020-03-18 12:28
 */
@Configuration
@EnableScheduling
@Slf4j
public class Test {

    @Autowired
    BlogCrawlerService blogCrawlerService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    private void test(){
        log.info(Objects.toString(System.currentTimeMillis()));
        blogCrawlerService.updateCrawlerBlogs();
    }
}
