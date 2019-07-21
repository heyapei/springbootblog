package com.hyp.service;

import com.hyp.pojo.Article;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/7/21 17:53
 * @Description: TODO
 */
public interface ArticleService {
    /**
     * 添加文章内容
     * @param article 文章内容
     * @return 主键ID
     */
    int insertArticle(Article article);

    /**
     * 通过id查找文章内容
     * @param id 文档id
     * @return 文章详情
     */
    Article getArticleById(int id);
}
