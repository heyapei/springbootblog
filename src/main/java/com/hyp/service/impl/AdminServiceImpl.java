package com.hyp.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.mapper.AdminMapper;
import com.hyp.pojo.Article;
import com.hyp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 18:13
 * @Description: TODO
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Article getArticleById(int id) {
        return adminMapper.getArticleById(id);
    }

    @Override
    public List<Article> getArticleByUserId(int userId) {
        return adminMapper.getArticleByUserId(userId);
    }

    @Override
    public PageInfo<Article> getArticleByUserIdAndPage(int userId, int currentPage, int pageSize) {

        PageHelper.startPage(currentPage, pageSize);
        List<Article> list = adminMapper.getArticleByUserId(userId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public void getAll() {
        List<Article> articles = adminMapper.getAllArticle();
        System.out.println("全部内容"+articles);
    }


}
