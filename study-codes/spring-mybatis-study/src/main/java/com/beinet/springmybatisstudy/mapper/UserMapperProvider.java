package com.beinet.springmybatisstudy.mapper;

import com.beinet.springmybatisstudy.entity.User;
import org.springframework.util.StringUtils;

public class UserMapperProvider {
    /**
     * 根据对象，动态生成SQL
     *
     * @param user 对象
     * @return sql
     */
    public String getDynamicSql(User user) {
        if (user == null) {
            throw new RuntimeException("condition can't be null");
        }
        StringBuilder sb = new StringBuilder();
        if (user.getId() != null && user.getId() > 0) {
            appendCond(sb, "id=#{id}");
        }
        if (!StringUtils.isEmpty(user.getName())) {
            appendCond(sb, "name like concat('%', #{name}, '%')"); // like的写法
        }

        if (sb.length() == 0)
            return "select * from user";
        sb.insert(0, "select * from user where ");
        return sb.toString();
    }

    private void appendCond(StringBuilder sb, String cond) {
        if (sb.length() > 0) {
            sb.append(" and ");
        }
        sb.append(cond);
    }
}
