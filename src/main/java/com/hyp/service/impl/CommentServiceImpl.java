package com.hyp.service.impl;

import com.hyp.mapper.CommentMapper;
import com.hyp.pojo.Comment;
import com.hyp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/21 21:44
 * @Description: TODO
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentByArticleId(int articleId) {
        return commentMapper.getCommentByArticleId(articleId);
    }
}
