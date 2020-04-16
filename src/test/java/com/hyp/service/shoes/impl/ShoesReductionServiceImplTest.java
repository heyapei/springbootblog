package com.hyp.service.shoes.impl;

import com.hyp.pojo.shoes.dataobject.ShoesReduction;
import com.hyp.service.shoes.ShoesReductionService;
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
 * @Date 2020/2/22 11:43
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesReductionServiceImplTest {
    @Autowired
    private ShoesReductionService shoesReductionService;


    @Test
    public void addShoesReduction() {
        ShoesReduction shoesReduction = new ShoesReduction();
        shoesReduction.setEvent("提交数据");
        shoesReduction.setPhoneNum("15518901416");
        shoesReduction.setReduction(10);
        shoesReduction.setCreateDate(new Date());

        /*boolean b = shoesReductionService.addShoesReduction(shoesReduction);
        System.out.println(b);*/
    }

    @Test
    public void getShoesReduction() {
        ShoesReduction shoesReduction = new ShoesReduction();
        shoesReduction.setEvent("提交数据");
        shoesReduction.setPhoneNum("15518901416");
        shoesReduction.setReduction(10);
        shoesReduction.setCreateDate(new Date());
        List<ShoesReduction> shoesReduction1 = shoesReductionService.getShoesReduction(shoesReduction);
        System.out.println(shoesReduction1.toString());
    }

    @Test
    public void deleteShoesReduction() {
        boolean b = shoesReductionService.deleteShoesReduction(1);
        System.out.println(b);
    }
}