package com.hyp.pojo.shoes.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "shoes_order_item")
@Mapper
public class ShoesOrderItem {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;
    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Integer orderId;

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
    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endDate;

}