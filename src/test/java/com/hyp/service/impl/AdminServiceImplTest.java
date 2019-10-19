package com.hyp.service.impl;

import com.github.pagehelper.PageInfo;
import com.hyp.pojo.Article;
import com.hyp.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/2 21:57
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminServiceImplTest {
    @Autowired
    private AdminService adminService;

    @Test
    public void getArticleByUserIdAndPage() {
        PageInfo<Article> articleByUserIdAndPage = adminService.getArticleByUserIdAndPage(2, 10, 1);
        System.out.println(articleByUserIdAndPage);
    }

    @Test
    public void getAll() {

        adminService.getAll();
    }
}