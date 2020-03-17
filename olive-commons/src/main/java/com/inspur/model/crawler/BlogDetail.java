package com.inspur.model.crawler;

import lombok.Data;

@Data
public class BlogDetail {

     private Long intId;

     private String title;

     private String url;

     private byte[] content;
}
