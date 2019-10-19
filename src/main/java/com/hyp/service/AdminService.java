package com.hyp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.pojo.Article;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 18:12
 * @Description: TODO
 */
public interface AdminService {

    /**
     * 通过id查找对应的article
     * @param id article的Id
     * @return
     */
    Article getArticleById(int id);


    /**
     * 查询个人用户的日记
     * @param userId 用户Id
     * @return
     */
    List<Article> getArticleByUserId(int userId);


    /**
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<Article> getArticleByUserIdAndPage(int userId,int currentPage, int pageSize) ;

    void getAll();


}
