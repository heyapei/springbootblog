package com.hyp.pojo.shoes.dataobject;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/31 22:40
 * @Description: TODO 订单详情表
 */
@Data
@Table(name = "shoes_product")
@Mapper
public class ShoesProduct {
    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 属性
     */
    private String attribute;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 修改时间
     */
    @Column(name = "modify_date")
    private Date modifyDate;

    /**
     * 状态值 0：正常
     */
    private Integer state;

    /**
     * 商品码
     */
    private String code;

    /**
     * 颜色
     */
    private String color;

    /**
     * 大小
     */
    private Integer size;

    /**
     * 描述
     */
    private String description;
    /**
     * 数量
     */
    private Integer number;

}