package com.hyp.mapper;

import com.hyp.pojo.Article;
import com.hyp.utils.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/6/23 17:46
 * @Description: TODO
 */
@Repository
public interface ArticleMapper extends MyMapper<Article> {
    /**
     * 保存文章
     * @param article
     * @return 主键ID
     */
    int addArticle(Article article);

    /**
     * 更新文章
     * @param article 文章内容
     * @return 主键ID
     */
    int updateArticle(Article article);

    Article getArticleById(int id);
}