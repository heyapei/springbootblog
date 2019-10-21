package com.hyp.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/21 21:41
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;


    @Test
    public void getCommentByArticleId() {
        System.out.println(commentMapper.getCommentByArticleId(1));
    }
}