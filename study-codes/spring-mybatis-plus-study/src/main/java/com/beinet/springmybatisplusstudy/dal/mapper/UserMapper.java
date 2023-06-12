package com.beinet.springmybatisplusstudy.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beinet.springmybatisplusstudy.dal.entity.User;

// 未指定@DS注解，会使用默认的主数据源，就是application.yml里定义的xxx
public interface UserMapper extends BaseMapper<User> {
}
