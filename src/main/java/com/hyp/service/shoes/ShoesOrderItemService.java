package com.hyp.service.shoes;

import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/2 17:03
 * @Description: TODO
 */
public interface ShoesOrderItemService {

    /**
     * 查询最近countNum个热销产品
     * @param countNum
     * @return
     */
    List<ShoesOrderItem> hotSaleCount(int countNum,String startDate,String endDate);


    /**
     * 通过orderId删除orderItem
     *
     * @param orderId
     * @return
     */
    int deleteOrderItemByOrderId(int orderId);


    /**
     * 更新orderItem
     *
     * @param shoesOrderItem
     * @return
     */
    int updateShoesOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem);


    /**
     * 删除orderItem
     *
     * @param shoesOrderItem
     * @return
     */
    int delShoesOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem);


    /**
     * 获取orderItem
     *
     * @param shoesOrderItem
     * @return
     */
    List<ShoesOrderItem> getShoesOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem);

    /**
     * 通过ShoesOrderItem查询数据
     *
     * @param shoesOrderItem
     * @return
     */
    List<ShoesOrderItem> getOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem);


    /**
     * 添加订单详情
     *
     * @param shoesOrderItem
     * @return
     */
    int addOrderItem(ShoesOrderItem shoesOrderItem);
}
