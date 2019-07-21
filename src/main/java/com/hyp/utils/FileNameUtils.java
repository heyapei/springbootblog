package com.hyp.utils;

import java.util.UUID;

public class FileNameUtils {


    /**
     * @Method: makeFileName
     * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
     * @author: heyapei
     * @param filename 文件的原始名称
     * @return uuid+"_"+文件的原始名称
     */
    public String makeFileName(String filename){  //2.jpg
        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUIDUtil.getUUID() + "_" + filename;
    }


}
