package com.hyp.controller.admin;

import com.hyp.pojo.Article;
import com.hyp.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/2 23:22
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void showOneArticle() {
        Article articleByUserId = adminService.getArticleById(1);
        System.out.println(articleByUserId);
    }
}