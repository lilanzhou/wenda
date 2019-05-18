package com.ryan.controller;

import com.ryan.model.Comment;
import com.ryan.model.EntityType;
import com.ryan.model.HostHolder;
import com.ryan.model.Question;
import com.ryan.service.CommentService;
import com.ryan.service.LikeOrDisLikeService;
import com.ryan.service.QuestionService;
import com.ryan.service.UserService;
import com.ryan.util.JSONUtil;
import com.ryan.util.SaltUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Administrator on 2019:05:01
 *
 * @Author : Lilanzhou
 * 功能 :
 */

@Controller
public class QuestionController {
        private static final Logger logger = LoggerFactory.getLogger(QuestionController .class);
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeOrDisLikeService likeService;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setTitle(title);
            if (hostHolder.getUser() == null) {
                question.setUserId(JSONUtil.ANONYMOUS_USERID);
                // return WendaUtil.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return JSONUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return JSONUtil.getJSONString(1, "失败");
    }
    @RequestMapping(value = "/question/{qid}",method = {RequestMethod.GET})
    public String getQuestionDetail(Model model, @PathVariable("qid") int qid){
        Question question = questionService.getQuestion(qid);
        model.addAttribute("question",question);
        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);

        List<Map<String,Object>>mapList=new ArrayList<>();

        for (Comment comment:commentList) {
            Map<String ,Object>map=new HashMap<>();
            map.put("comment",comment);
            if(hostHolder.getUser()==null){
                map.put("liked",0);
            }else {
                map.put("liked",likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));

            }
            map.put("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            map.put("user",userService.getUser(comment.getUserId()));
            mapList.add(map);
        }
       model.addAttribute("comments",mapList);

        return "detail";
    }
}
