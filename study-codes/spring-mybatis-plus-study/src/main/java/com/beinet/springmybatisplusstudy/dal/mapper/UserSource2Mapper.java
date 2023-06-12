package com.beinet.springmybatisplusstudy.dal.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beinet.springmybatisplusstudy.dal.entity.User;

// 使用指定的数据源
@DS("dyn-source2")
public interface UserSource2Mapper extends BaseMapper<User> {
}
