package com.inspur.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @author wang.ning
 * @create 2020-02-20 11:29
 */
public class FileUtils {

    /**
     * 处理第一级别文件夹名称
     * @param name
     * @return
     */
    public static String generateArcgsiNameL1(String name){
        name = name.indexOf("L") > -1 ? name.substring(1,name.length()):name;
        DecimalFormat df=new DecimalFormat("00");
        return "L"+df.format(Integer.parseInt(name));
    }

    /**
     * 处理第二级别文件夹名称
     * @param name
     * @return
     */
    public static String generateArcgisNameL2(String name){
        return "";
    }


    public static void main(String[] args) {


        System.out.println(Integer.toHexString(11).toUpperCase());;

        File file = new File("F:\\Tiles\\");
        if(file.isDirectory()){
            File[] filesL1 = file.listFiles();
            for(File f:filesL1){
                //处理L1级别的文件命名
                f.renameTo(new File(f.getParent()+ "\\"+generateArcgsiNameL1(f.getName())));
                //处理第二级文件命名
                /*if(f.isDirectory()){
                    File[] filesL2 = file.listFiles();
                    for(File f2:filesL2){

                    }
                }*/
            }
        }
    }
}
