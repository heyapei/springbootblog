package com.hyp.controller.comment;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.pojo.Comment;
import com.hyp.service.CommentService;
import com.hyp.utils.returncore.Result;
import com.hyp.utils.returncore.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/10/21 21:59
 * @Description: TODO
 */

@RestController
@RequestMapping("comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 通过articleId获取当前评论
     *
     * @param articleId 文章Id
     * @param page      页码
     * @param size      每页数量
     * @return 封装好的json数据
     */
    @GetMapping(value = "/getComment/{articleId}/{page}/{size}")
    public Result<Object> showOneArticle(@PathVariable int articleId,
                                         @PathVariable Integer page,
                                         @PathVariable Integer size) {
        PageHelper.startPage(page, size);
        // 查询文档下面的所有的评论
        List<Comment> commentByArticleId = commentService.getCommentByArticleId(articleId);
        PageInfo pageInfo = new PageInfo(commentByArticleId);
        log.info("文章Id:{}如下{}", articleId, commentByArticleId);
        Result<Object> objectResult = ResultGenerator.genSuccessResult(pageInfo);
        return objectResult;
    }


}
