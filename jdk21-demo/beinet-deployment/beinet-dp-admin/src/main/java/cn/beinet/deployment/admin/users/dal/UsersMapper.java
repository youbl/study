package cn.beinet.deployment.admin.users.dal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.beinet.deployment.admin.users.dal.entity.Users;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author youbl.blog.csdn.net
 * @since 2024-11-19 12:28:00
 */
public interface UsersMapper extends BaseMapper<Users> {
    String TABLE = "users";

    @Select("<script>" +
            "SELECT * FROM " + TABLE + " a " +
            "WHERE a.id IN " +
            "<foreach item='item' index='index' collection='idList' open='(' separator=',' close=')'>" +
            " #{item} " +
            "</foreach> " +
            "ORDER BY a.id DESC" +
            "</script>")
    List<Users> getListByIds(List<Long> idList);
}