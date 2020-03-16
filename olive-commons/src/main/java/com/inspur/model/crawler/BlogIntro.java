package com.inspur.model.crawler;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang.ning
 * @create 2020-02-21 8:22
 */
@Data
public class BlogIntro implements Serializable {

    private Long intId;

    private String title;

    private String summary;

    private String url;
}
