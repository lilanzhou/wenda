package com.ryan.service;

import com.ryan.dao.LoginTicketDAO;
import com.ryan.dao.UserDao;
import com.ryan.model.LoginTicket;
import com.ryan.model.User;

import com.ryan.util.SaltUtil;
import freemarker.template.utility.StringUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDAO;

    @Autowired
    private LoginTicketDAO  loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
    public void logout(String ticket,int status){
        loginTicketDAO.updateStatus(ticket,status);
    }
    public Map<String,Object> login(String username,String password){
        Map<String,Object>map=new HashMap<>();
        if(" ".equals(username)){//username==null
            map.put("msg","用户名不能为空！");
            return map;
        }
        if(" ".equals(password)){
            map.put("msg","密码不能为空！");
            return map;
        }
        User user=userDAO.findByName(username);
        if(user==null){
            map.put("msg","用户名不存在！");
            return map;
        }
        if(!SaltUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码不正确！");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }
    public Map<String,Object> register(String username,String password){
        Map<String,Object>map=new HashMap<>();
        if(" ".equals(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if(" ".equals(password)){
            map.put("msg","密码不能为空！");
            return  map;
        }
        User user=userDAO.findByName(username);
        if(user!=null){
            map.put("msg","用户已经被注册！");
            return map;
        }
       //注册 密码加强 加salt
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(SaltUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);

        return ticket.getTicket();
    }
    public User findByName(String username){
       return userDAO.findByName(username);
    }

}
