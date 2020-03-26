package com.inspur.api.crawler;

/**
 * @author wang.ning
 * @create 2020-03-18 12:40
 */
public interface IBlogCrawlerService {

    public void crawlerBlogs();

    public void updateCrawlerBlogs();

    public void writeBlogsURLToEhcache();
}
