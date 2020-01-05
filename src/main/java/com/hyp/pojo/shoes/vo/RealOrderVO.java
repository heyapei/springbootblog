package com.hyp.pojo.shoes.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/5 16:08
 * @Description: TODO
 */
@Data
public class RealOrderVO {
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
}
