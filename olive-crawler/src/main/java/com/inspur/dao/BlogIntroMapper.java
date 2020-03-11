package com.inspur.dao;

import com.inspur.api.crawler.bean.BlogIntro;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogIntroMapper {

    public List<BlogIntro> getAllBlogIntro();

    public BlogIntro getBlogIntroById(Long id);

    public List<BlogIntro> getBlogIntroByTitle(String title);

    public int saveBlogIntro(BlogIntro blogIntro);

}
