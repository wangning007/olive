package com.inspur.enums;

import lombok.Data;

/**
 * @author wang.ning
 * @create 2020-03-27 12:49
 */
public enum  ResponseEnum {

    SUCCESS(200,"成功!");

    private int code;  //状态码

    private String memo; //描述

    private ResponseEnum(int code,String memo){
        this.code = code;
        this.memo = memo;
    }

    public int getCode() {
        return code;
    }
}
