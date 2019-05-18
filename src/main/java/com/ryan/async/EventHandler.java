package com.ryan.async;

import java.util.List;

/**
 * Created by Administrator on 2019:05:04
 *
 * @Author : Lilanzhou
 * 功能 :
 */
public interface EventHandler {
    //事件处理函数
    void doHandle(EventModel eventModel);
    //获取该事件处理器所支持的事件类型
    List<EventType>getSupportEventTypes();
}
