package com.beinet.springmybatisstudy.mapper;

import com.beinet.springmybatisstudy.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where age>#{agePara}")
    List<User> findByAgeGt(int agePara);

    @Select("select * from user where age<=#{agePara}")
    List<User> findByAgeLet(@Param("agePara") int num);

    // Options注解的目的，是返回主键给user对象
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into user(name, age, email) values(#{name}, #{age}, #{email})")
    int add(User user);

    @Delete("delete from user where id=#{id}")
    int deleteById(long id);

    @Update("update user set name=#{name},age=#{age}, email=#{email} where id=#{id}")
    int update(User user);

}
