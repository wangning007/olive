package com.inspur;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wang.ning
 * @create 2020-03-11 12:28
 */
//@EnableCaching
@MapperScan("com.inspur.dao")
@SpringBootApplication
public class AppAplication {

    public static void main(String[] args) {
        SpringApplication.run(AppAplication.class);
    }
}
