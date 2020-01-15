package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesOrderItemMapper;
import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;
import com.hyp.service.shoes.ShoesOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/4 17:19
 * @Description: TODO
 */
@Service
public class ShoesOrderItemServiceImpl implements ShoesOrderItemService {
    @Autowired
    private ShoesOrderItemMapper shoesOrderItemMapper;


    @Override
    public List<ShoesOrderItem> hotSaleCount(int countNum,String startDate,String endDate) {
        return shoesOrderItemMapper.hotSaleCount(countNum,startDate,endDate);
    }

    @Override
    public int deleteOrderItemByOrderId(int orderId) {
        Example example = new Example(ShoesOrderItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        return shoesOrderItemMapper.deleteByExample(example);
    }

    @Override
    public int updateShoesOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem) {
        return shoesOrderItemMapper.updateByPrimaryKeySelective(shoesOrderItem);
    }

    @Override
    public int delShoesOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem) {
        return shoesOrderItemMapper.delete(shoesOrderItem);
    }

    @Override
    public List<ShoesOrderItem> getShoesOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem) {
        return shoesOrderItemMapper.select(shoesOrderItem);
    }

    @Override
    public List<ShoesOrderItem> getOrderItemByShoesOrderItem(ShoesOrderItem shoesOrderItem) {
        Example example = new Example(ShoesOrderItem.class);
        example.orderBy("createDate").desc();
        Example.Criteria criteria = example.createCriteria();
        if (shoesOrderItem != null) {
            // 通过订单号
            if (shoesOrderItem.getId() != null) {
                criteria.andEqualTo("id", shoesOrderItem.getId());
            }
            // 通过订单号
            if (shoesOrderItem.getOrderId() != null) {
                criteria.andEqualTo("orderId", shoesOrderItem.getOrderId());
            }
            // 通过状态值查询
            if (shoesOrderItem.getProductId() != null) {
                criteria.andEqualTo("productId", shoesOrderItem.getProductId());
            }

        }
        List<ShoesOrderItem> shoesOrderItemList = shoesOrderItemMapper.selectByExample(example);
        return shoesOrderItemList;
    }

    @Override
    public int addOrderItem(ShoesOrderItem shoesOrderItem) {
        return shoesOrderItemMapper.insertSelective(shoesOrderItem);
    }
}
