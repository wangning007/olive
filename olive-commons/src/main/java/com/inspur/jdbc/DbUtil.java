package com.inspur.jdbc;

import org.apache.commons.dbutils.QueryRunner;

/**
 * @author wang.ning
 * @create 2020-02-22 15:25
 */
public class DbUtil {

    private static QueryRunner queryRunner = null;

    public static QueryRunner getQueryRunner(){
        return queryRunner == null ? new QueryRunner():queryRunner;
    }
}
