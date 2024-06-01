package com.beinet.springmybatisplusstudy.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName(value = "user123")
@Accessors(chain = true)
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @TableField(exist = false)
    private String extension;// 不是数据库字段
}