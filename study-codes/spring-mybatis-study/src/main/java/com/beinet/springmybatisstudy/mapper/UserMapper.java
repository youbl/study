package com.beinet.springmybatisstudy.mapper;

import com.beinet.springmybatisstudy.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    String TABLE_NAME = "user";

    @Select({"<script>select * from " + TABLE_NAME + " where name in (" +
            "<foreach collection='names' open='' item='item' separator=','> " +
            "#{item}" +
            "</foreach> ) </script>"})
    List<User> findByNameArr(List<String> names);

    @Select("select * from " + TABLE_NAME + " where age>#{agePara}")
    List<User> findByAgeGt(int agePara);

    @Select("select * from " + TABLE_NAME + " where age<=#{agePara}")
    List<User> findByAgeLet(@Param("agePara") int num);

    // Options注解的目的，是返回主键给user对象
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into " + TABLE_NAME + "(name, age, email) values(#{name}, #{age}, #{email})")
    int add(User user);

    @Delete("delete from " + TABLE_NAME + " where id=#{id}")
    int deleteById(long id);

    @Update("update " + TABLE_NAME + " set name=#{name},age=#{age}, email=#{email} where id=#{id}")
    int update(User user);

    //批量插入数据, separator为逗号
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({"<script> insert into " + TABLE_NAME + "(name, age, email) " +
            "values " +
            "<foreach collection='list' open='' item='item' separator=','> " +
            "(#{item.name},#{item.age},#{item.email}) " +
            "</foreach> </script>"})
    int batchInsert(List<User> list);

    //批量更新数据, separator为分号
    @Update({"<script><foreach collection='list' open='' item='item' separator=';'> " +
            "update " + TABLE_NAME + " set name=#{item.name},age=#{item.age}, email=#{item.email} where id=#{item.id} " +
            "</foreach> </script>"})
    int batchUpdate(List<User> list);

    // 动态SQL查询
    @SelectProvider(type = UserMapperProvider.class, method = "getDynamicSql")
    List<User> findByCond(User user);
}
