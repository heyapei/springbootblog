package com.hyp.service.shoes.impl;

import com.hyp.service.shoes.ShoesOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/1 11:40
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesOrderServiceImplTest {

    @Autowired
    private ShoesOrderService shoesOrderService;
    @Test
    public void addShoesOrder() {
    }

    @Test
    public void updateShoesOrder() {
    }

    @Test
    public void getShoesOrderByOrderId() {
    }

    @Test
    public void getShoesOrderByUserId() {
    }

    @Test
    public void getShoesOrderByDateRange() {
    }

    @Test
    public void getTodayShoesOrder() {
        Map<String, String> todayShoesOrder = shoesOrderService.getTodayShoesOrder("2019-12-31 0:0:0", "2019-12-31 23:51:53");
        log.info("今日数据：{}",todayShoesOrder);
    }
}