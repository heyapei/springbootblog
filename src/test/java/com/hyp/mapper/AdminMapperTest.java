package com.hyp.mapper;

import com.hyp.pojo.Article;
import com.hyp.pojo.Comment;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/2 22:12
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdminMapperTest {

    @Autowired
    private AdminMapper adminMapper;



    @Test
    public void testPojo() {

    }

    @Test
    public void getAllArticle() {
        List<Article> allArticle = adminMapper.getAllArticle();
        Assert.assertNotNull(allArticle);
        System.out.println(allArticle);
    }

    /*@Test
    public void getArticleById() {
        Article articleById = adminMapper.getArticleById(1);
        Assert.assertNotNull(articleById);
    }*/
}