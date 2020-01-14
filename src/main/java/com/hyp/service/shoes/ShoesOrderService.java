package com.hyp.service.shoes;

import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/31 22:40
 * @Description: TODO
 */
public interface ShoesOrderService {

    /**
     * 通过Id删除shoesOrder和OrderItem
     *
     * @param orderId
     * @return
     */
    int deleteOrderAndOrderItem(int orderId);


    /**
     * 创建shoesOrder
     *
     * @param shoesOrder
     * @return
     */
    int addShoesOrder(ShoesOrder shoesOrder);

    /**
     * 更新shoesOrder
     *
     * @param shoesOrder
     * @return
     */
    int updateShoesOrder(ShoesOrder shoesOrder);

    /**
     * 通过OrderID获取订单信息
     *
     * @param orderId
     * @return
     */
    ShoesOrder getShoesOrderByOrderId(int orderId);

    /**
     * 通过shoesOrder获取订单信息
     *
     * @param shoesOrder
     * @return
     */
    ShoesOrder getShoesOrderByShoesOrder(ShoesOrder shoesOrder);

    /**
     * 通过shoesOrder获取订单信息
     *
     * @param shoesOrder
     * @return
     */
    List<ShoesOrder> getShoesOrderByPhoneAndTime(ShoesOrder shoesOrder);

    /**
     * 通过userID获取用户的订单信息
     *
     * @param userId
     * @return
     */
    List<ShoesOrder> getShoesOrderByUserId(int userId);


    /**
     * 查询时间范围内的订单
     *
     * @param startDate 格式yyyy-MM-dd HH:mm:SS
     * @param endDate   格式yyyy-MM-dd HH:mm:SS
     * @return
     */
    List<ShoesOrder> getShoesOrderByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询今天的订单数量和金额
     *
     * @param startDate 格式yyyy-MM-dd HH:mm:SS
     * @param endDate   格式yyyy-MM-dd HH:mm:SS
     * @return
     */
    Map<String, String> getTodayShoesOrder(@Param("startDate") String startDate, @Param("endDate") String endDate);


}
