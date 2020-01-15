package com.hyp.mapper;

import com.hyp.pojo.shoes.dataobject.ShoesOrderItem;
import com.hyp.pojo.shoes.utils.DatesUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/1/15 14:00
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesOrderItemMapperTest {
    @Autowired
    private ShoesOrderItemMapper shoesOrderItemMapper;

    @Test
    public void testHotSaleCount() {
        List<ShoesOrderItem> shoesOrderItems = shoesOrderItemMapper.hotSaleCount(20,"2019-01-05 17:10:17","2020-01-15 14:21:07");
        log.debug(shoesOrderItems.toString());
    }

    @Test
    public void testDatesUtils() {
        Date beginDayOfWeek = DatesUtil.getBeginDayOfWeek();
        Date endDayOfWeek = DatesUtil.getEndDayOfWeek();
        System.out.println(beginDayOfWeek + "==" + endDayOfWeek);
    }
}