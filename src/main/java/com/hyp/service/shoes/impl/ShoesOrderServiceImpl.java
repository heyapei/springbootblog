package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesOrderMapper;
import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.service.shoes.ShoesOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/1 11:33
 * @Description: TODO
 */
@Slf4j
@Service
public class ShoesOrderServiceImpl implements ShoesOrderService {
    @Autowired
    private ShoesOrderMapper shoesOrderMapper;

    @Override
    public int addShoesOrder(ShoesOrder shoesOrder) {
        return shoesOrderMapper.insertSelective(shoesOrder);
    }

    @Override
    public int updateShoesOrder(ShoesOrder shoesOrder) {
        return shoesOrderMapper.updateByPrimaryKeySelective(shoesOrder);
    }

    @Override
    public ShoesOrder getShoesOrderByOrderId(int orderId) {
        Example example = new Example(ShoesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        List<ShoesOrder> shoesOrderList = shoesOrderMapper.selectByExample(example);
        if (shoesOrderList != null && shoesOrderList.size() > 0) {
            return shoesOrderList.get(0);
        }
        return null;
    }

    @Override
    public ShoesOrder getShoesOrderByShoesOrder(ShoesOrder shoesOrder) {
        Example example = new Example(ShoesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (shoesOrder != null) {
            // 通过订单号
            if (shoesOrder.getId() != null) {
                criteria.andEqualTo("id", shoesOrder.getId());
            }
            // 通过状态值查询
            if (shoesOrder.getState() != null) {
                criteria.andEqualTo("state", shoesOrder.getState());
            }
            // 通过userId查询
            if (shoesOrder.getUserId() != null) {
                criteria.andEqualTo("userId", shoesOrder.getUserId());
            }

            List<ShoesOrder> shoesOrderList = shoesOrderMapper.selectByExample(example);

            log.info(shoesOrderList.toString());

            if (shoesOrderList != null && shoesOrderList.size() > 0) {
                return shoesOrderList.get(0);
            }

        }

        return null;
    }

    @Override
    public List<ShoesOrder> getShoesOrderByPhoneAndTime(ShoesOrder shoesOrder) {
        Example example = new Example(ShoesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (shoesOrder != null) {

            // 通过userId查询
            if (shoesOrder.getUserId() != null) {
                criteria.andEqualTo("userId", shoesOrder.getUserId());
            }
            if (shoesOrder.getCreateDate() != null) {
                criteria.andLessThanOrEqualTo("createDate", shoesOrder.getCreateDate());

            }

            if (shoesOrder.getEndDate() != null) {
                criteria.andGreaterThanOrEqualTo("createDate", shoesOrder.getEndDate());
            }

            List<ShoesOrder> shoesOrderList = shoesOrderMapper.selectByExample(example);

            log.info(shoesOrderList.toString());

            if (shoesOrderList != null && shoesOrderList.size() > 0) {
                return shoesOrderList;
            }

        }

        return null;
    }

    @Override
    public List<ShoesOrder> getShoesOrderByUserId(int userId) {
        return null;
    }

    @Override
    public List<ShoesOrder> getShoesOrderByDateRange(String startDate, String endDate) {
        List<ShoesOrder> shoesOrderByDateRange = shoesOrderMapper.getShoesOrderByDateRange(startDate, endDate);
        return shoesOrderByDateRange;
    }

    @Override
    public Map<String, String> getTodayShoesOrder(String startDate, String endDate) {
        Map<String, String> todayOrder = new HashMap<>(2);
        BigDecimal money = new BigDecimal(0f);
        List<ShoesOrder> shoesOrderByDateRange = shoesOrderMapper.getShoesOrderByDateRange(startDate, endDate);
        for (ShoesOrder shoesOrder : shoesOrderByDateRange) {
            money = money.add(shoesOrder.getMoney());
        }
        todayOrder.put("todayMoney", String.valueOf(money));
        todayOrder.put("todayOrderCount", String.valueOf(shoesOrderByDateRange.size()));
        return todayOrder;
    }
}
