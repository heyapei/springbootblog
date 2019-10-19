package com.hyp.controller.admin;

import com.github.pagehelper.PageInfo;
import com.hyp.pojo.Article;
import com.hyp.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 17:16
 * @Description: TODO
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class Admin {

    @Autowired
    private AdminService adminService;

    /*@RequestMapping(value = "/index/{userId}/{page}/{size}", method = RequestMethod.GET)
    public String articleDetailByUserIdAndPage(@PathVariable int userId,
                                @PathVariable("page") Integer page,
                                @PathVariable("size") Integer size,
                                ModelMap map) {
        PageInfo<Article> articleByUserIdAndPage = adminService.getArticleByUserIdAndPage(userId, page, size);
        return "admin/index";
    }*/

    @RequestMapping(value = "/index/{userId}", method = RequestMethod.GET)
    public String articleDetailByUserId(@PathVariable int userId,
                                ModelMap map) {
        List<Article> articleByUserId = adminService.getArticleByUserId(userId);
        map.put("articles", articleByUserId);
        return "admin/index";
    }


    @RequestMapping(value = "/index/{articleId}", method = RequestMethod.GET)
    public String showOneArticle(@PathVariable int articleId,
                                        ModelMap map) {
        Article articleByUserId = adminService.getArticleById(articleId);
        map.put("article", articleByUserId);
        return "user/single-post";
    }

}
