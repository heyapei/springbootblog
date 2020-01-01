package com.hyp.pojo.shoes.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/31 22:40
 * @Description: TODO 订单详情表
 */
@Data
@Table(name = "shoes_order")
@Mapper
public class ShoesOrder {
    /**
     * 订单id
     */
    @Id
    private Integer id;

    /**
     * 用户Id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 操作人员Id
     */
    @Column(name = "system_id")
    private Integer systemId;

    /**
     * 订单总金额
     */
    private BigDecimal money;

    /**
     * 状态 默认为0（创建中）
     */
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createDate;

    /**
     * 订单完结时间
     */
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endDate;

}