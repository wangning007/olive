package com.inspur.geo;

import com.inspur.geo.model.SimpleCoodinates;
import com.inspur.geo.utils.WgsGcjConverter;
import com.inspur.jdbc.ConnectionUtil;
import com.inspur.jdbc.DbUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author wang.ning
 * @create 2020-02-22 14:59
 */
public class Test {
    private static final Logger logger = Logger.getLogger(Test.class);
    private static Connection connection = null;

    public static void main(String[] args) {

        try {
            QueryRunner queryRunner = null;
            connection = ConnectionUtil.getConnection();
            queryRunner = DbUtil.getQueryRunner();
            String sql = "select int_id,longitude,latitude,zh_label from jike_juli";
            List list = queryRunner.execute(connection, sql, new MapListHandler());
            logger.error("数据数量:"+((List) list.get(0)).size());
            List<Map<String,String>> dataList = (List<Map<String, String>>) list.get(0);
            for(Map<String,String> dataMap:dataList){
                String int_id = dataMap.get("INT_ID");
                String longitude = dataMap.get("LONGITUDE");
                String latitude = dataMap.get("LATITUDE");
                String zh_label = dataMap.get("ZH_LABEL");

                //gcj02转84
                SimpleCoodinates coodinates = WgsGcjConverter.gcj02ToWgs84(Double.parseDouble(latitude),Double.parseDouble(longitude));
                //Srid
                //String geoSridSql = "SELECT  srid FROM sde.st_geometry_columns WHERE table_name = 'GEO_JIKE_POINT'";

                //objectid
                String objectIdSql = " select SDE.VERSION_USER_DDL.NEXT_ROW_ID('GIS_PLATFORM',( " +
                        " select c.REGISTRATION_ID as id " +
                        " from SDE.SPATIAL_REFERENCES a, SDE.LAYERS b, SDE.TABLE_REGISTRY c " +
                        " where b.SRID = a.SRID and b.OWNER = c.OWNER " +
                        " and b.TABLE_NAME = c.TABLE_NAME and c.table_name= 'GEO_JIKE_POINT' " +
                        " and c.OWNER = 'GIS_PLATFORM')) AS objectid " +
                        " from dual";

                Object objectid = queryRunner.query(connection,objectIdSql,new ScalarHandler());

                String shape = "sde.st_point ("+coodinates.getLon()+","+coodinates.getLat()+",4326)";
                //插入空间库
                String GEOSQL = "insert into GIS_PLATFORM.GEO_JIKE_POINT(objectid,int_id,zh_label,shape) values " +
                        " ("+Integer.parseInt(objectid.toString())+",'"+dataMap.get("int_id")+"','"+dataMap.get("zh_label")+"',"+shape+")";

                int flag = queryRunner.update(connection,GEOSQL);
                logger.info(flag);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
