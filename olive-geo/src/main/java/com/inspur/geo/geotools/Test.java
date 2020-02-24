package com.inspur.geo.geotools;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;

/**
 * @author wang.ning
 * @create 2020-02-24 11:53
 */
public class Test {

    public static void main(String[] args) {
        File file = new File("F:\\中国省地市区县边界数据\\省边界\\省边界.shp");
        FileDataStore store = null;
        SimpleFeatureSource featureSource = null;

        try {
            store = FileDataStoreFinder.getDataStore(file);
            featureSource = store.getFeatureSource();
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureIterator simpleFeatureIterator = featureCollection.features();
            while(simpleFeatureIterator.hasNext()){
                SimpleFeature simpleFeature = simpleFeatureIterator.next();
                System.out.println(simpleFeature.getAttributes());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
