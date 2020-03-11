package com.inspur;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wang.ning
 * @create 2020-03-11 12:28
 */
@MapperScan("com.inspur.dao")
@SpringBootApplication
public class AppAplication {

    public static void main(String[] args) {
        SpringApplication.run(AppAplication.class);
    }
}
