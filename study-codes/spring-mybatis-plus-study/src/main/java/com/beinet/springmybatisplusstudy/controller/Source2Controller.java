package com.beinet.springmybatisplusstudy.controller;

import com.beinet.springmybatisplusstudy.dal.entity.User;
import com.beinet.springmybatisplusstudy.dal.mapper.UserSource2Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2021/9/18 21:31
 */
@RestController
@RequiredArgsConstructor
public class Source2Controller {
    private final UserSource2Mapper userMapper;

    @GetMapping("user2/{id}")
    public User getById(@PathVariable int id) {
        return userMapper.selectById(id);
    }

    /**
     * 返回全部用户
     *
     * @return
     */
    @GetMapping("user2/all")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("user2/add")
    public User addUser() {
        User user = new User();
        user.setName("adddddd");
        user.setAge(456);
        userMapper.insert(user);
        return user;
    }

}
