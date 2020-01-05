package com.hyp.service.shoes.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyp.mapper.ShoesOrderItemMapper;
import com.hyp.mapper.ShoesOrderMapper;
import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;
import com.hyp.service.shoes.ShoesUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/2 13:16
 * @Description: TODO
 */
@Service
public class ShoesUserServiceImpl implements ShoesUserService {

    @Autowired
    private ShoesOrderMapper shoesOrderMapper;

    @Autowired
    private ShoesOrderItemMapper shoesOrderItemMapper;

    @Override
    public JSONObject showBuyTrendByUserId(int userId) {
        JSONObject returnJson = new JSONObject();
        // 当前年度
        Calendar calendar = Calendar.getInstance();

        /*计算选课量*/
        ArrayList<String> dateTime = new ArrayList<>();
        ArrayList<Integer> selectCount = new ArrayList<>();

        int orderNum = 0;
        double countMoney = 0;

        for (int num = 3; num >= 0; num--) {
            int year = calendar.get(Calendar.YEAR) - num;
            for (int i = 1; i <= 12; i++) {
                StringBuilder s = new StringBuilder();
                if (i < 10) {
                    s.append("0").append(i);
                } else {
                    s.append(i);
                }
                dateTime.add(year + "-" + s.toString());
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, i - 1);
                Example example = new Example(ShoesOrder.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("userId", userId);
                String startTime = year + "-" + s.toString() + "-01";
                String endTime = year + "-" + s.toString() + 1 + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
                criteria.andLessThanOrEqualTo("endDate", endTime);
                criteria.andGreaterThanOrEqualTo("endDate", startTime);
                List<ShoesOrder> shoesOrders = shoesOrderMapper.selectByExample(example);
                // 订单数量
                orderNum += shoesOrders.size();
                // 鞋子数量
                int orderItemNum = 0;
                for (ShoesOrder shoesOrder : shoesOrders) {
                    Example example1 = new Example(ShoesOrderItem.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("orderId", shoesOrder.getId());
                    List<ShoesOrderItem> shoesOrderItems = shoesOrderItemMapper.selectByExample(example1);
                    orderItemNum += shoesOrderItems.size();
                    // 金额
                    countMoney += shoesOrder.getMoney().doubleValue();
                }


                selectCount.add(orderItemNum);
            }
        }


        returnJson.put("orderNum", orderNum);
        returnJson.put("countMoney", countMoney);
        returnJson.put("dateTime", dateTime);
        returnJson.put("buyCount", selectCount);
        return returnJson;
    }
}
