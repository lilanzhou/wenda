package com.ryan.controller;

import com.ryan.model.Comment;
import com.ryan.model.EntityType;
import com.ryan.model.HostHolder;
import com.ryan.service.CommentService;
import com.ryan.service.QuestionService;
import com.ryan.util.JSONUtil;
import com.ryan.util.SensitiveUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * Created by Administrator on 2019:05:01
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Controller
public class CommentController {
    private static final Logger LOGGER= LoggerFactory.getLogger(CommentController.class);
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    SensitiveUtil sensitiveUtil;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = "/addComment",method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content")String content){
        try{
            content= HtmlUtils.htmlEscape(content);
            content=sensitiveUtil.filter(content);

            Comment comment=new Comment();
            if(hostHolder.getUser()!=null){
                comment.setUserId(hostHolder.getUser().getId());
            }else {
                comment.setUserId(JSONUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setStatus(0);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            //添加评论
            commentService.addComment(comment);

            //更新评论数量
            int commentCount = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),commentCount);

        }catch (Exception e){
            LOGGER.error("添加评论失败"+e.getMessage());
        }

        return "redirect:/question/"+String.valueOf(questionId);
    }

}
