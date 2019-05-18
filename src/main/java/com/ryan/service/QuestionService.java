package com.ryan.service;

import com.ryan.dao.QuestionDAO;
import com.ryan.model.Question;
import com.ryan.util.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/15.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveUtil sensitiveUtil;
    public List<Question> getLatestQuestions(int userId,int offset,int end) {
        return questionDAO.selectLatestQuestions(userId,offset,end);
    }

    public int addQuestion(Question question){
        //html过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setTitle(sensitiveUtil.filter(question.getTitle()));
        question.setContent(sensitiveUtil.filter(question.getContent()));
        return questionDAO.addQuestion(question)>0 ?question.getId():1;
    }
    public Question getQuestion(int id){
        return questionDAO.getById(id);
    }

    public int updateCommentCount(int id,int count){
        return questionDAO.updateCommentCount(id,count);
    }
}
