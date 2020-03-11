package com.inspur.api.crawler.bean;

import lombok.Data;

@Data
public class BlogDetail {

     private Long intId;

     private String title;

     private String url;

     private byte[] content;
}
