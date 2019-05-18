package com.ryan.async;

/**
 * Created by Administrator on 2019:05:04
 *
 * @Author : Lilanzhou
 * 功能 :
 */
public enum EventType {
    //事件的类型
     LIKE(0),
     COMMENT(1),
    LOGIN(2),
    MAIL(3);
    private int value;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
