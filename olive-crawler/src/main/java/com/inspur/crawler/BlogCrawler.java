package com.inspur.crawler;

import com.inspur.jdbc.ConnectionUtil;
import com.inspur.utils.SnowFlakeIdGenerator;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BlogCrawler {

    private static final Logger logger = Logger.getLogger(BlogCrawler.class);

    private static Connection connection = null;

    private static final String URL_INDEX  = "https://www.cnblogs.com/cate/java/";

    private static final String URL = "https://www.cnblogs.com/cate/java/#p2";

    private static void analysisIntroduction(){

    }

    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);

        //模拟浏览器
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        RequestConfig requestConfig = RequestConfig.custom()
                                .setConnectionRequestTimeout(10000)
                                .setConnectTimeout(10000).setSocketTimeout(10000).build();
        httpGet.setConfig(requestConfig);

        //存放 二级URL
        List<String> urls = new ArrayList<String>();

        try{
            HttpResponse response = client.execute(httpGet);
            String conteStr = EntityUtils.toString(response.getEntity(),"utf-8");
            Document document = Jsoup.parse(conteStr);
            Elements elements = document.select("#post_list .post_item .post_item_body");

            for(Element element:elements){
                Elements titles = element.select("h3 a");

                String title = titles.get(0).text();
                String url = titles.get(0).attr("href");

                Elements summarys = element.select(".post_item_summary");
                String summary = summarys.get(0).text();

                QueryRunner queryRunner = new QueryRunner();
                String sql = "insert into blog_intro (int_id,title,summary,url) values(?,?,?,?)";

                connection = ConnectionUtil.getConnection();
                Long intId = new SnowFlakeIdGenerator(1,2).nextId();
                Object[] objects = {1,title,summary,url};
                int flag = queryRunner.update(connection,sql,objects);
                logger.info(flag);
                //作者  时间

                //请求内容
                HttpGet contentGet = new HttpGet(url);
                response = client.execute(contentGet);
                Document contentDoc = Jsoup.parse(EntityUtils.toString(response.getEntity()));
                Element contentElement = contentDoc.getElementById("cnblogs_post_body");
                String content = contentElement.html();

                Element titleElement = contentDoc.getElementById("cb_post_title_url");
                String contentTitle = titleElement.text();

                String contentSql = "insert into blog_detail (int_id,title,contenta) values(?,?,?)";
                Long contentId = new SnowFlakeIdGenerator(1,2).nextId();
                Object[] contentParams = {contentId,contentTitle,content.getBytes()};

                int contentFlag = queryRunner.update(connection,contentSql,contentParams);
                logger.info(contentFlag);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
