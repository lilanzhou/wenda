package com.ryan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by Administrator on 2019:04:25
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Controller
//@RequestMapping("/hello")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    @RequestMapping("/request/{param}")
    @ResponseBody
    public String request(HttpServletResponse response,HttpServletRequest request,
    @PathVariable(value = "param") String param){
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append("name:"+request.getHeader(name)+"<br/>");
        }
        for (Cookie c :request.getCookies()
                ) {
            sb.append("Cookie==:"+c.getName()+"==:"+c.getValue());

        }
        sb.append("getMethod:" + request.getMethod() + "<br>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br>");
        sb.append("getQueryString:" + request.getQueryString() + "<br>");
        sb.append("getRequestURI:" + request.getRequestURI() + "<br>");
        return sb.toString();
    }

    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "myid", defaultValue = "默认cookie") String myId,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "MyId From Cookie:" + myId;
    }

    /**
     *
     *  重定向
     * @param code  301 重定向，302永久转向
     * @param httpSession
     * @return  转向首页
     */
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key,HttpSession session) {

        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("Key 错误"+session.getAttribute("msg"));
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error" + e.getMessage();
    }
    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId")String groupId,
                          @PathVariable("userId")int userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue = "ryan") String ryan){

       return String.format("GroupId:%s,UserId:%d,Type:%d,Key:%s",groupId,userId,type,ryan);
    }


}

