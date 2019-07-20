package com.hyp.controller.old;

import com.hyp.pojo.IMoocJSONResult;
import com.hyp.pojo.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloContoller {

    @Autowired
    private Resource resource;

    @RequestMapping("/hello")
    public Object hello() {
        return "hello springboot~~";
    }

    @RequestMapping("/getResource")
    public IMoocJSONResult getResource() {
        Resource bean = new Resource();
        /**
         * 属性的拷贝
         */
        BeanUtils.copyProperties(resource, bean);
        return IMoocJSONResult.ok(bean);
    }

}
