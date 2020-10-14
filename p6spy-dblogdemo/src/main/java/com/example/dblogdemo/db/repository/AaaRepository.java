package com.example.dblogdemo.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AaaRepository extends JpaRepository<Aaa, Long> {

    @Query(nativeQuery = true, value = "select * from aaa where id>?1 and num<?2")
    List<Aaa> find1(long id, int num);


    @Query(nativeQuery = false, value = "select a from Aaa a where id>?1 and num<?2")
    List<Aaa> find2(long id, int num);
}
