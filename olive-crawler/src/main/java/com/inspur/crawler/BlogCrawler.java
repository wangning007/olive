package com.inspur.crawler;

import com.inspur.jdbc.ConnectionUtil;
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

public class BlogCrawler {

    private static final Logger logger = Logger.getLogger(BlogCrawler.class);

    private static Connection connection = null;

    private static void analysisIntroduction(){

    }

    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://www.cnblogs.com/cate/java/");

        //模拟浏览器
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        RequestConfig requestConfig = RequestConfig.custom()
                                .setConnectionRequestTimeout(10000)
                                .setConnectTimeout(10000).setSocketTimeout(10000).build();
        httpGet.setConfig(requestConfig);

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
                Object[] objects = {1,title,summary,url};
                int flag = queryRunner.update(connection,sql,objects);
                logger.info(flag);


                //作者  时间

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
