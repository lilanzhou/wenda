package com.ryan.model;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019:04:29
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Component
public class HostHolder {
    private static ThreadLocal<User>users=new ThreadLocal<>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
