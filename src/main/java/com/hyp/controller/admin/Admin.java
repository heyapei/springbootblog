package com.hyp.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.mapper.AdminMapper;
import com.hyp.pojo.Article;
import com.hyp.service.AdminService;
import com.hyp.utils.returncore.Result;
import com.hyp.utils.returncore.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/1 17:16
 * @Description: TODO
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class Admin {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Article> list = adminMapper.getAllArticle();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/index/{userId}/{page}/{size}", method = RequestMethod.GET)
    public Result articleDetailByUserIdAndPage(@PathVariable int userId,
                                               @PathVariable("page") Integer page,
                                               @PathVariable("size") Integer size) {
        PageInfo articleByUserIdAndPage = adminService.getArticleByUserIdAndPage(userId, page, size);
        return ResultGenerator.genSuccessResult(articleByUserIdAndPage);
    }

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
