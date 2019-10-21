package com.hyp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/21 21:24
 * @Description: TODO 评论表
 */
@Data
@Table(name = "comment")
@Mapper
public class Comment {
    @Id
    private int id;
    @Column(name = "article_id")
    private int articleId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "evaluate_content")
    private String evaluateContent;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;


}
