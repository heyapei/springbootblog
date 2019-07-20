package com.hyp.controller.old;

import com.hyp.pojo.IMoocJSONResult;
import com.hyp.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 20/27 19:32
 * @Description: TODO 使用RestController
 * @RestController = @Controller +  @ResponseBody
 *  @ResponseBody 作用是直接返回字符串 不指向视图
 */

@RestController
public class UserController1 {

    @RequestMapping("/1")
    public User getUser(){
        User u = new User();
        u.setName("heyapei");
        u.setAge(19);
        u.setBirthday(new Date());
        u.setPassword("12321");
        u.setDesc("fist");
        return u;
    }


    @RequestMapping("/userJson1")
    public IMoocJSONResult getUserJson(){
        User u = new User();
        u.setName("heyapei");
        u.setAge(39);
        u.setBirthday(new Date());
        u.setPassword("12321");
        u.setDesc("发送方发送的123");
        return IMoocJSONResult.ok(u);
    }
}
