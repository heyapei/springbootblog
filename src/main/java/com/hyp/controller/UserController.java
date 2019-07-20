package com.hyp.controller;

import com.hyp.pojo.IMoocJSONResult;
import com.hyp.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/5/27 19:32
 * @Description: TODO 使用原生的Controller，这个简单一点 只不过是有点麻烦了
 * 下面学习的是RestController 这个就是Controller和ResponseBody作用的综合
 */

@Controller
public class UserController {

    @RequestMapping("/")
    @ResponseBody
    public User getUser(){
        User u = new User();
        u.setName("heyapei");
        u.setAge(19);
        u.setBirthday(new Date());
        u.setPassword("12321");
        u.setDesc("fist");
        return u;
    }


    @RequestMapping("/userJson")
    @ResponseBody
    public IMoocJSONResult getUserJson(){
        User u = new User();
        u.setName("heyapei");
        u.setAge(19);
        u.setBirthday(new Date());
        u.setPassword("12321");
        u.setDesc("fist");
        return IMoocJSONResult.ok(u);
    }
}
