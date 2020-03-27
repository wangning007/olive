package com.inspur.utils;

import com.inspur.model.crawler.BlogIntro;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析文档工具类
 * @author wang.ning
 * @create 2020-03-27 15:56
 */
@Slf4j
@Component
public class DocumentUtil {

    @Autowired
    SnowFlakeIdGenerator snowFlakeIdGenerator;
    /**
     * 解析博客园一页的简介结构数据
     * @param document
     * @return
     */
    public List<BlogIntro> parseCnBlogsIntro(Document document){
        List<BlogIntro> blogIntros = new ArrayList<>();
        synchronized (document){
            Elements elements = document.select(" .post_item_body");
            log.info("blog intro..size:"+elements.size());
            if(elements.size() > 0){
                for(Element element:elements) {
                    Elements titles = element.select("h3 a");
                    String elementTitle = titles.get(0).text();
                    String elementUrl = titles.get(0).attr("href");
                    Elements summarys = element.select(".post_item_summary");
                    String summary = summarys.get(0).text();
                    //Long intId = new SnowFlakeIdGenerator(1,2).nextId();
                    Long intId = snowFlakeIdGenerator.nextId();
                    //int flag = queryRunner.update(ConnectionUtil.getConnection(),sql,intId,elementTitle,summary,elementUrl);
                    BlogIntro blogIntro = new BlogIntro();
                    blogIntro.setIntId(intId);
                    blogIntro.setSummary(summary);
                    blogIntro.setUrl(elementUrl);
                    blogIntro.setTitle(elementTitle);
                    blogIntros.add(blogIntro);
                }
            }
        }
        return blogIntros;
    }
}
