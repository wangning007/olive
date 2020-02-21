package com.inspur.crawler;

import com.inspur.jdbc.ConnectionUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang.ning
 * @create 2020-02-19 19:27
 */
public class Test {

    private static final Logger logger = Logger.getLogger(Test.class);
    private static Connection connection = null;

    public static void main(String[] args) {
        try {
            connection = ConnectionUtil.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select contenta from blog_detail where int_id = 428199230282145792";
            List<Map> list = queryRunner.execute(connection, sql, new ResultSetHandler<Map>() {
                public Map handle(ResultSet resultSet) throws SQLException {
                    Map map = new HashMap();
                    while(resultSet.next()){
                        map.put("contenta",new String(resultSet.getBytes("contenta")));
                    }
                    return map;
                }
            });
            System.out.println("数据数量:"+list.get(0).get("contenta"));
            logger.error("数据数量:"+list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
