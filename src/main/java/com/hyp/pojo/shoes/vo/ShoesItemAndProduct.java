package com.hyp.pojo.shoes.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/14 15:22
 * @Description: TODO
 */
@Data
public class ShoesItemAndProduct {
    private String name;
    private String color;
    private Integer size;
    private BigDecimal price;
    private Integer number;

}
