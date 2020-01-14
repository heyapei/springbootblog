package com.hyp.service.shoes.impl;

import com.hyp.mapper.ShoesOrderItemMapper;
import com.hyp.service.shoes.ShoesOrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/7 22:07
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesOrderItemServiceImplTest {

    @Autowired
    private ShoesOrderItemService shoesOrderItemService;

    @Test
    public void deleteOrderItemByOrderId() {
        shoesOrderItemService.deleteOrderItemByOrderId(15);
    }
}