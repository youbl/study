package com.beinet.springmybatisstudy.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    private String extension;// 不是数据库字段
}