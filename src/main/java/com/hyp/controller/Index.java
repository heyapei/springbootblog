package com.hyp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 13:26
 * @Description: TODO 默认页转发到首页
 */
@Controller
@Slf4j
public class Index {
    @RequestMapping("/")
    public String index() {
        log.info("转发到首页");
        return "forward:/user/index";
    }


}
