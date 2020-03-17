package com.inspur.dao;

import com.inspur.model.crawler.BlogDetail;
import com.inspur.model.crawler.BlogIntro;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogIntroMapper {

    public List<BlogIntro> getAllBlogIntro();

    public BlogIntro getBlogIntro(BlogIntro blogIntro);

    public int saveBlogIntro(BlogIntro blogIntro);

    public int saveBlogDetail(BlogDetail blogDetail);
    public int updateBlogIntro(BlogIntro blogIntro);

    public int deleteBlogIntro(BlogIntro blogIntro);

}
