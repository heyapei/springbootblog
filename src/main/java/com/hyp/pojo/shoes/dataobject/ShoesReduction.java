package com.hyp.pojo.shoes.dataobject;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/31 22:40
 * @Description: TODO 数据抵免情况
 */
@Data
@Mapper
@Table(name = "shoes_reduction")
public class ShoesReduction {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "phone_num")
    private String  phoneNum;

    /**
     * 抵免的数据
     */
    private Integer reduction;

    /**
     * 事件内容
     */
    private String event;

    /**
     * 创建事件
     */
    @Column(name = "create_date")
    private Date createDate;

}