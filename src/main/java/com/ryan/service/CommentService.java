package com.ryan.service;

import com.ryan.dao.CommentDAO;
import com.ryan.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019:05:01
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;


    public List<Comment> getCommentByEntity(int entityId, int entityType) {

        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }
    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }
    public void deleteByEntity(int entityId,int entityType){
        commentDAO.updateStatus(entityId,entityType,1);
    }
    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }
}
