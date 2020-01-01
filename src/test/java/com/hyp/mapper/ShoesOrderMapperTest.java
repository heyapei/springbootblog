package com.hyp.mapper;

import com.hyp.pojo.shoes.dataobject.ShoesOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/31 23:24
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesOrderMapperTest {

    @Autowired
    private ShoesOrderMapper shoesOrderMapper;

    @Test
    public void addShoesOrder() {
        ShoesOrder shoesOrder = new ShoesOrder();
        shoesOrder.setState(0);
        shoesOrder.setMoney(new BigDecimal(100f));
        shoesOrder.setSystemId(1);
        shoesOrder.setUserId(1);

        int i = shoesOrderMapper.addShoesOrder(shoesOrder);
        log.info("返回值{}", i);
        log.info("返回的主键{}", shoesOrder.getId());
    }

    @Test
    public void updateShoesOrder() {
        ShoesOrder shoesOrder = new ShoesOrder();
        shoesOrder.setState(0);
        shoesOrder.setMoney(new BigDecimal(100.35f));
        shoesOrder.setSystemId(1);
        shoesOrder.setUserId(1);
        shoesOrder.setId(2);
        int i = shoesOrderMapper.updateShoesOrder(shoesOrder);
        log.info("返回值{}", i);
        log.info("返回的主键{}", shoesOrder.getId());
    }

    @Test
    public void getShoesOrderByOrderId() {
        ShoesOrder shoesOrderByOrderId = shoesOrderMapper.getShoesOrderByOrderId(2);
        log.info(shoesOrderByOrderId.toString());
    }

    @Test
    public void getShoesOrderByUserId() {
        List<ShoesOrder> shoesOrderByOrderId = shoesOrderMapper.getShoesOrderByUserId(1);
        for (ShoesOrder shoesOrder : shoesOrderByOrderId) {
            log.info(shoesOrder.toString());
        }

    }

    @Test
    public void getShoesOrderByDateRange() {
        List<ShoesOrder> shoesOrderByOrderId = shoesOrderMapper.getShoesOrderByDateRange("2019-12-31 0:0:0", "2019-12-31 23:51:53");
        for (ShoesOrder shoesOrder : shoesOrderByOrderId) {
            log.info(shoesOrder.toString());
        }
    }
}