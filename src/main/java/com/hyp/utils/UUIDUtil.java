package com.hyp.utils;

import java.util.UUID;

/**
 * 生成文件名
 * @author : heypei
 */
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
