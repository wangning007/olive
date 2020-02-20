package com.inspur.crawler;

import com.inspur.jdbc.ConnectionUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

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
            String sql = "select username from sta_user where username = ?";
            List<Map> list = queryRunner.execute(connection, sql, new ResultSetHandler<Map>() {
                public Map handle(ResultSet resultSet) throws SQLException {
                    Map map = new HashMap();
                    while(resultSet.next()){
                        map.put("username",resultSet.getString("username"));
                    }
                    return map;
                }
            },"wangning");
            System.out.println("数据数量:"+list.size());
            logger.error("数据数量:"+list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
