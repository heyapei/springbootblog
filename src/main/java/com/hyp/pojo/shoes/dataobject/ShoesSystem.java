package com.hyp.pojo.shoes.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 13:15
 * @Description: TODO 管理员实体表
 */
@Data
@Table(name = "shoes_system")
@Mapper
public class ShoesSystem {
    @Id
    @Column(name = "id")
    private Integer userId;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "pass_word")
    private String passWord;
    @Column(name = "phone_num")
    private String phoneNum;
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;
}
