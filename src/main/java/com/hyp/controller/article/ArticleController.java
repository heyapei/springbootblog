package com.hyp.controller.article;

import com.hyp.pojo.Article;
import com.hyp.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/7/21 14:59
 * @Description: TODO article相关操作
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/showAdd")
    public String showAdd() {
        return "wangEditor/wangEditor";
    }

    @RequestMapping(value = "/addArticle", method = RequestMethod.POST)
    public String addArticle(@RequestParam Map<String, Object> params) {
        System.out.println(params.get("article").toString());
        Article article = new Article();
        article.setArticleContent(params.get("article").toString());
        article.setDescription("description");
        article.setSort("sort");
        article.setTitle("title");
        article.setState(10);
        article.setRange(11);
        article.setTitle("title");
        article.setShowOrder(1);
        article.setUserId(1);
        article.setUserName("何亚培");
        log.info("添加article：" + article.toString());
        articleService.insertArticle(article);
        return "wangEditor/wangEditor";
    }


    /**
     * 回显数据到wangEditor
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/articleDetail/{id}", method = RequestMethod.GET)
    public String articleDetail(@PathVariable int id, ModelMap map) {
        Article article = articleService.getArticleById(id);
        map.addAttribute("article", article);
        log.info("传入回显的数据：" + article.toString());
        return "wangEditor/wangEditor";
    }
}

