package com.inspur.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryUtil {

    private static SqlSessionFactory sqlSessionFactory = null;

    private static final Class CLASS_LOCK = SqlSessionFactoryUtil.class;

    private SqlSessionFactoryUtil(){}

    public static SqlSessionFactory initSqlSessionFactory(){
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            synchronized (CLASS_LOCK){
                if(sqlSessionFactory == null){
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    public static SqlSession openSqlSession(){
        if(sqlSessionFactory == null){
            initSqlSessionFactory();
        }
        return sqlSessionFactory.openSession();
    }

}
