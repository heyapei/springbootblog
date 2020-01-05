package com.hyp.pojo.shoes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/5 18:14
 * @Description: TODO
 */
@Data
public class OrderItemVO {
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 名称
     */
    private String name;
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
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 价格
     */
    private BigDecimal money;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createDate;
}
