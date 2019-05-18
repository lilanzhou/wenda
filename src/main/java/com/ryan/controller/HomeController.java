package com.ryan.controller;

import com.ryan.model.Question;
import com.ryan.service.QuestionService;
import com.ryan.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019:04:27
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Controller

public class HomeController {
    @Resource
    QuestionService questionService;
    @Autowired
    UserService userService;

    /**
     * 显示信息
     *查询得到问题并返回问题集合
     */
    public List<Map<String,Object>>getQuestion(int userId,int offset,int end){
        List<Question> listQuestion = questionService.getLatestQuestions(userId,offset,end);
        List<Map<String,Object>> typeList=new ArrayList<>();
        for (Question question:listQuestion) {
            Map<String,Object> vo=new HashMap();
            vo.put("question",question);
            vo.put("user",userService.getUser(question.getUserId()));
            typeList.add(vo);
        }

        return typeList;
    }

    /**
     *   展示问题集合的首页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("list",getQuestion(0,0,10));
        return "index";
    }

    /**
     * 通过用户的id查询自己的问题集合
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId")int userId){
        model.addAttribute("list",getQuestion(userId,0,10));
        return "index";
    }
}
