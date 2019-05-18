package com.ryan.service;

import com.ryan.dao.MessageDAO;
import com.ryan.model.Message;
import com.ryan.util.SensitiveUtil;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019:05:03
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
   @Autowired
    SensitiveUtil sensitiveUtil;
    public int addMessage(Message message) {
        message.setContent(sensitiveUtil.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }


    public List<Message> getConversationDetail(String conversationId, int offset, int end) {
        return messageDAO.getConversationDetail(conversationId, offset, end);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }


   public List<Message> getConversationList(int userId, int offset, int end) {
        return messageDAO.getConversationList(userId, offset, end);
    }
    public void updateHasRead(String conversationId){
        messageDAO.updateHasRead(conversationId);
    }
}
