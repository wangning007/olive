package com.inspur.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author wang.ning
 * @create 2020-03-18 12:28
 */
@Configuration
@EnableScheduling
@Slf4j
public class Test {

    @Scheduled(cron = "0/5 * * * * ?")
    private void test(){
        log.info("测试.....");
    }
}
