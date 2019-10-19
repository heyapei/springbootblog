package com.hyp.mapper;

import com.hyp.pojo.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 18:13
 * @Description: TODO
 */
@Repository
public interface AdminMapper {

    /**
     * 查询个人用户的日记
     * @param userId 用户Id
     * @return
     */
    List<Article> getArticleByUserId(int userId);


    /**
     * 查询所有的article
     * @return
     */
    List<Article> getAllArticle();

    /**
     * 通过id查找对应的article
     * @param id article的Id
     * @return
     */
    Article getArticleById(int id);
}
