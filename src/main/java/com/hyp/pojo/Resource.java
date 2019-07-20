package com.hyp.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/5/27 19:31
 * @Description: TODO
 */
@Configuration
@ConfigurationProperties(prefix = "com.imooc.opensource")
@PropertySource(value = "classpath:resource.properties")
public class Resource {




    private String name;
    private String website;
    private String language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @PropertySource 设置资源文件路径地址 项目打包完成后文件会存在classpath中的
     * @ConfigurationProperties 前缀 最后读取就会读取前缀后面的代码所对应的值然后映射到实体类中的属性
     */
}
