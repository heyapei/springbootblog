package com.hyp.mapper;

import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 14:02
 * @Description: TODO
 */
@Repository
public interface ShoesOrderMapper extends MyMapper<ShoesOrder> {
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


}