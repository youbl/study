package com.example.dblogdemo.db.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Aaa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long restId;
    private int dishhour;
    private long dishId;
    private int num;
}
