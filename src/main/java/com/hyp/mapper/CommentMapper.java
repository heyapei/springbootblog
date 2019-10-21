package com.hyp.mapper;

import com.hyp.pojo.Article;
import com.hyp.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/21 21:24
 * @Description: TODO
 */
@Repository
public interface CommentMapper {

    /**
     * 查询个人用户的日记
     *
     * @param articleId 文章id
     * @return
     */
    List<Comment> getCommentByArticleId(int articleId);

}
