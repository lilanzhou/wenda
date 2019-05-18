package com.ryan.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryan.model.HostHolder;
import com.ryan.model.Message;
import com.ryan.model.User;
import com.ryan.service.MessageService;
import com.ryan.service.UserService;
import com.ryan.util.JSONUtil;
import com.ryan.util.SensitiveUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Administrator on 2019:05:03
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Controller
public class MessageController {
    private static final Logger LOGGER= LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    SensitiveUtil sensitiveUtil;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try{
            if(hostHolder.getUser()==null){
                return JSONUtil.getJSONString(999,"未登录");
            }
            User user=userService.findByName(toName);
            if(user==null){
                return JSONUtil.getJSONString(1,"用户不存在");
            }
            Message message=new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            int toId=message.getToId();
            int fromId=message.getFromId();
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);
            return JSONUtil.getJSONString(0);

        }catch (Exception e){
            LOGGER.error("添加信息失败！"+e.getMessage());
            return JSONUtil.getJSONString(1,"添加站内信失败");
        }
    }


    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
     try {
          int localUserId=hostHolder.getUser().getId();
         List<Map<String,Object>> mapList=new ArrayList<>();
         List<Message> messageList = messageService.getConversationList(localUserId, 0, 10);
         for(Message message:messageList){
             Map<String,Object>map= new HashMap<>();
             map.put("message",message);
             int targetId=message.getFromId()==localUserId?message.getToId():message.getFromId();
             map.put("user",userService.getUser(targetId));
             map.put("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
             mapList.add(map);
         }
         model.addAttribute("conversations",mapList);
     }catch (Exception e){
         LOGGER.error("获取站内列表失败"+e.getMessage());
     }
     return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId")String conversationId) {

        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<Map<String,Object>> mapList=new ArrayList<>();
            for(Message message:messageList){
                Map<String,Object>map=new HashMap<>();
                map.put("message",message);
                User user=userService.getUser(message.getFromId());
                if(user==null){
                    continue;
                }
                map.put("headUrl",user.getHeadUrl());
                map.put("userId",user.getId());
                mapList.add(map);
            }
            model.addAttribute("messages",mapList);

//            Message message=new Message();
//            message.setHasRead(1);
            messageService.updateHasRead(conversationId);

        }catch (Exception e){
            LOGGER.error("获取站内信息失败"+e.getMessage());

        }
        return "letterDetail";
    }
}
