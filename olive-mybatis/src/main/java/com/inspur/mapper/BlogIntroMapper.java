package com.inspur.mapper;

import com.inspur.api.crawler.bean.BlogIntro;

import java.util.List;

public interface BlogIntroMapper {

    public List<BlogIntro> getAllBlogIntro();

    public BlogIntro getBlogIntroById(Long id);

    public List<BlogIntro> getBlogIntroByTitle(String title);

}
