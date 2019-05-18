package com.ryan.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019:05:04
 *
 * @Author : Lilanzhou
 * 功能 :定义事件的实体
 */
public class EventModel {
    private EventType type;//事件的类型
    private int actorId;//触发者的id
    //entityType和entityId共同组成了触发的事件
    private int entityType;
    private int entityId;

    private int entityOwnerId;//该事件的拥有者
    private Map<String,String> exts=new HashMap<>();//需要传输的额外信息

    public EventModel() {
    }
    public EventModel(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventModel setExt(String key,String value) {
        exts.put(key,value);
        this.exts = exts;
        return this;
    }
}
