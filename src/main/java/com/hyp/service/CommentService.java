package com.hyp.service;

import com.hyp.pojo.Comment;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/21 21:43
 * @Description: TODO
 */
public interface CommentService {

    /**
     * 查询个人用户的日记
     *
     * @param articleId 文章id
     * @return
     */
    List<Comment> getCommentByArticleId(int articleId);
}
