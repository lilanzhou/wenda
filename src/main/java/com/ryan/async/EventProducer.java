package com.ryan.async;

import com.alibaba.fastjson.JSONObject;
import com.ryan.util.JedisAdapter;
import com.ryan.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019:05:04
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;


    //触发事件
    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
