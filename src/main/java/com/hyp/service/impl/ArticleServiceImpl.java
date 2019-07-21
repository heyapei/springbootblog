package com.hyp.service.impl;

import com.hyp.mapper.ArticleMapper;
import com.hyp.pojo.Article;
import com.hyp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/6/23 17:54
 * @Description: TODO
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int insertArticle(Article article) {
        int i = articleMapper.addArticle(article);
        return i;
    }

    @Override
    public Article getArticleById(int id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        return article;
    }
}
