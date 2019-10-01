package com.hyp.controller.staticrequest;

import com.hyp.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 14:45
 * @Description: TODO
 */
@Controller
@RequestMapping("/static")
@Slf4j
public class StaticRequest {

    @RequestMapping(value = "/page/{html}", method = RequestMethod.GET)
    public String page(@PathVariable String html) {
        log.info("静态资源跳转"+html);
        return "user/"+html;
    }

}
