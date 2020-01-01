package com.hyp.pojo.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/12/1 12:42
 * @Description: TODO 添加注解忽略没有属性值
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NewDTO {
    private String uniquekey;
    private String title;
    private String date;
    private String category;
    private String author_name;
    private String url;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;
    private String thumbnail_pic_s03;
}
