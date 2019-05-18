package com.ryan.dao;

import com.ryan.model.Comment;
import com.ryan.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2019:05:03
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME="message";
    String INSERT_FIELD="from_id,to_id,content,created_date,has_read,conversation_id";
    String SELECT_FIELD="id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,") values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select ",SELECT_FIELD,"from ",TABLE_NAME ,"where conversation_id=#{conversationId} limit #{offset},#{end}"})
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,
                                 @Param("offset")int offset,
                                 @Param("end")int end);
    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select ", INSERT_FIELD, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    @Update({"update ",TABLE_NAME," set has_read=1 where conversation_id=#{conversationId}" })
    int updateHasRead(@Param("conversationId")String conversationId);
}
