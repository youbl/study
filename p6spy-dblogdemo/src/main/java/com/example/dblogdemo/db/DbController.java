package com.example.dblogdemo.db;


import com.example.dblogdemo.db.repository.Aaa;
import com.example.dblogdemo.db.repository.AaaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DbController {

    @Autowired
    AaaRepository repostory;

    @GetMapping("dbAll")
    public List<Aaa> getAll() {
        List<Aaa> ret = repostory.find1(1, 200);
        ret.addAll(repostory.find2(1, 300));
        return ret;
    }


    @GetMapping("db")
    public Aaa getAll(@RequestParam long id) {
        return repostory.findById(id).orElse(null);
    }


    @GetMapping("dbupdate")
    public Aaa update(@RequestParam long id) {
        Aaa item = repostory.findById(id).orElse(null);
        if (item == null)
            return null;

        item.setDishhour(item.getDishhour() + 1);
        return repostory.save(item);
    }
}
