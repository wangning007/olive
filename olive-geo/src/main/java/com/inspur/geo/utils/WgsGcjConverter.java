package com.inspur.geo.utils;


import com.inspur.geo.model.SimpleCoodinates;

/**
 * @author wang.ning
 * @create 2019-11-27 9:23
 */
public class WgsGcjConverter {
    
    public static double M_PI = Math.PI;
    public static final double SEMI_MAJOR_AXIS = 6378245.0;
    public static final double FLATTENING = 0.00335233;
    public static final double SEMI_MINOR_AXIS = SEMI_MAJOR_AXIS * (1.0 - FLATTENING);
    private static final double a = SEMI_MAJOR_AXIS;
    private static final double b = SEMI_MINOR_AXIS;
    public static final double EE = (a * a - b * b) / (a * b);

    /**
     * wgs84 转 gcj02
     * @param wgsLat
     * @param wgsLon
     * @return
     */
    public static SimpleCoodinates wgs84ToGcj02(double wgsLat, double wgsLon) {
        if (isOutOfChina(wgsLat, wgsLon)) {
            return new SimpleCoodinates(wgsLat, wgsLon);
        }
        double dLat = transformLat(wgsLon - 105.0, wgsLat - 35.0);
        double dLon = transformLon(wgsLon - 105.0, wgsLat - 35.0);
        double radLat = wgsLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((SEMI_MAJOR_AXIS * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (SEMI_MAJOR_AXIS / sqrtMagic * Math.cos(radLat) * Math.PI);
        double gcjLat = wgsLat + dLat;
        double gcjLon = wgsLon + dLon;

        return new SimpleCoodinates(gcjLat, gcjLon);
    }

    /**
     * gcj02 转 wgs84
     * @param gcjLat
     * @param gcjLon
     * @return
     */
    public static SimpleCoodinates gcj02ToWgs84(double gcjLat, double gcjLon){
        SimpleCoodinates g0 = new SimpleCoodinates(gcjLat, gcjLon);
        SimpleCoodinates w0 = new SimpleCoodinates(g0);
        SimpleCoodinates g1 = wgs84ToGcj02(w0.getLat(), w0.getLon());
        SimpleCoodinates w1 = w0.substract(g1.substract(g0));
        while (maxAbsDiff(w1, w0) >= 1e-6) {
            w0 = w1;
            g1 = wgs84ToGcj02(w0.getLat(), w0.getLon());
            SimpleCoodinates gpsDiff = g1.substract(g0);
            w1 = w0.substract(gpsDiff);
        }

        return w1;
    }

    /**
     * 经纬度转墨卡托
     * @param lon
     * @param lat
     * @return
     */
    public static double[] lonLat2Mercator(double lon, double lat) {
        double[] xy = new double[2];
        double x = lon * 20037508.342789 / 180;
        double y = Math.log(Math.tan((90 + lat) * M_PI / 360)) / (M_PI / 180);
        y = y * 20037508.34789 / 180;
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    /**
     * 摩卡托转经纬度
     * @param mercatorX
     * @param mercatorY
     * @return
     */
    public static double[] Mercator2lonLat(double mercatorX, double mercatorY) {
        double[] xy = new double[2];
        double x = mercatorX / 20037508.34 * 180;
        double y = mercatorY / 20037508.34 * 180;
        y = 180 / M_PI * (2 * Math.atan(Math.exp(y * M_PI / 180)) - M_PI / 2);
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    private static double maxAbsDiff(SimpleCoodinates w1, SimpleCoodinates w0) {
        SimpleCoodinates diff = w1.substract(w0);
        double absLatDiff = Math.abs(diff.getLat());
        double absLonDiff = Math.abs(diff.getLon());

        return (absLatDiff > absLonDiff? absLatDiff: absLonDiff);
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret = ret + (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret = ret + (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret = ret + (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret = ret + (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret = ret + (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret = ret + (160.0 * Math.sin(y / 12.0 * Math.PI) + 320.0 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static boolean isOutOfChina(double wgsLat, double wgsLon) {
        if (wgsLat < 0.8293 || wgsLat > 55.8271) {
            return true;
        }
        if (wgsLon < 72.004 || wgsLon > 137.8347) {
            return true;
        }
        return false;
    }
}
