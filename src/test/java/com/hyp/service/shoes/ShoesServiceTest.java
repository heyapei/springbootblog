package com.hyp.service.shoes;

import com.hyp.pojo.shoes.dataobject.ShoesSystem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 14:26
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShoesServiceTest {

    @Autowired
    private ShoesService ShoesService;

    @Test
    public void shoesSystemUserLogin() {
        ShoesSystem shoesSystem = ShoesService.shoesSystemUserLogin("何亚培1", "123456");
        System.out.println("结果显示："+shoesSystem);
    }
}