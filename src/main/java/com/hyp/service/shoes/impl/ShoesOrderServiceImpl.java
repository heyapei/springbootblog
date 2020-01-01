package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesOrderMapper;
import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.service.shoes.ShoesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class ShoesOrderServiceImpl implements ShoesOrderService {
    @Autowired
    private ShoesOrderMapper shoesOrderMapper;

    @Override
    public int addShoesOrder(ShoesOrder shoesOrder) {
        return 0;
    }

    @Override
    public int updateShoesOrder(ShoesOrder shoesOrder) {
        return 0;
    }

    @Override
    public ShoesOrder getShoesOrderByOrderId(int orderId) {
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
