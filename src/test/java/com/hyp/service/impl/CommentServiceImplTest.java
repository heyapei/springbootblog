package com.hyp.service.impl;

import com.hyp.mapper.CommentMapper;
import com.hyp.pojo.Comment;
import com.hyp.service.CommentService;
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
 * @Date 2019/10/21 21:45
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CommentServiceImplTest {
    @Autowired
    private CommentService commentService;

    @Test
    public void getCommentByArticleId() {
        List<Comment> commentByArticleId = commentService.getCommentByArticleId(1);
        log.info("查询出来的数据:{}", commentByArticleId);
    }
}