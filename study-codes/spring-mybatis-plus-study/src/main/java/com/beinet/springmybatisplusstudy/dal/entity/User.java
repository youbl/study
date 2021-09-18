package com.beinet.springmybatisplusstudy.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @TableField(exist = false)
    private String extension;// 不是数据库字段
}