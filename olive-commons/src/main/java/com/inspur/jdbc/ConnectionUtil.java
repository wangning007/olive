package com.inspur.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author wang.ning
 * @create 2020-02-20 19:23
 */
public class ConnectionUtil {

    private static final String url;

    private static final String driver;

    private  static final String username;

    private static final String password;

    private static Connection connection = null;

    static {
        //ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc.properties");
        Properties prop = new Properties();
        try {
            prop.load(ConnectionUtil.class.getResourceAsStream("/jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = prop.getProperty("inspur.jdbc.driver");
        url = prop.getProperty("inspur.jdbc.url");
        username = prop.getProperty("inspur.jdbc.username");
        password = prop.getProperty("inspur.jdbc.password");
    }

    static {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception{
        return connection == null ?DriverManager.getConnection(url,username,password):connection;
    }


}
