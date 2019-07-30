package com.hyp.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/7/20 15:32
 * @Description: TODO 这里是用户前台页面
 */

@Controller
@RequestMapping("user")
public class UserController {

    /**
     * 访问网站的主界面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "user/gallery";
    }


}
