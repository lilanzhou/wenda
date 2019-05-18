package com.ryan.dao;

import com.ryan.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2019:05:01
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME="comment";
    String INSERT_FIELD="content,user_id,entity_id,entity_type,created_date,status";
    String SELECT_FIELD="id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,") values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME, " where id=#{id}"})
    Comment getCommentById(int id);

    @Update({"update ",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entityType=#{entityType}"})
    void updateStatus(@Param("entityId")int entityId,
                      @Param("entityType")int entityType,
                      @Param("status") int status);
    @Select({"select ",SELECT_FIELD,"from ",TABLE_NAME ,"where entity_id=#{entityId} and entity_type=#{entityType}"})
    List<Comment> selectByEntity(@Param("entityId")int entityId,
                                 @Param("entityType")int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);

}
