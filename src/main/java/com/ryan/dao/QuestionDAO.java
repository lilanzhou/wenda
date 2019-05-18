package com.ryan.dao;

import com.ryan.model.Question;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019:04:26
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Mapper
public interface QuestionDAO {
     String TABLE_NAME="question";
     String INSERT_FIELDS="title,content,user_id,created_date,comment_count";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS," ) values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);
    //注解和xml 形式
  @Select("<script>"
          +"select id ,"+INSERT_FIELDS+" from "+TABLE_NAME
          + "<if test='userId!=0'>"
          + " where user_id=#{userId} "
          +"</if>"
          +"order by id desc limit #{offset},#{end}"
          +"</script>")
     List<Question> selectLatestQuestions(@Param("userId") int userId,
                                          @Param("offset") int offset,
                                          @Param("end")int end);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Question getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

}
