package com.ryan.dao;

import com.ryan.model.User;
import jdk.nashorn.internal.objects.annotations.Where;
import org.apache.ibatis.annotations.*;

/**
 * Created by Administrator on 2019:04:26
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Mapper
public interface UserDao {
    String TABLE_NAME=" user ";
    String INSERT_FIELDS=" name,password,salt,head_url ";
    String SELECT_FIELDS=" id,"+INSERT_FIELDS;
    @Insert({"insert into ",TABLE_NAME, "(",INSERT_FIELDS ,") values(#{name},#{password},#{salt},#{headUrl})"})
     int addUser(User user);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
     User selectById(int id);
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
    User findByName(String name);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
     void updatePassword(User user);

    @Delete({"delete from ",TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
