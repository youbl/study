package beinet.cn.demojpaunittest;


import beinet.cn.demojpaunittest.entity.Aaa;
import beinet.cn.demojpaunittest.repository.AaaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DbController {

    private final AaaRepository repostory;

    public DbController(AaaRepository repostory) {
        this.repostory = repostory;
    }

    @GetMapping("dbAll")
    public List<Aaa> getAll() {
        return repostory.findAll();
    }


    @GetMapping("db")
    public Aaa getAll(@RequestParam long id) {
        return repostory.findById(id).orElse(null);
    }

    @GetMapping("dbupdate")
    public Aaa update(@RequestParam long id, @RequestParam(required = false) int num) {
        Aaa item = repostory.findById(id).orElse(null);
        if (item == null) {
            item = new Aaa();
            item.setId(id); // 插入时，setId是无效的
        }

        item.setNum(num);
        item.setDishhour(item.getDishhour() + 1);
        return repostory.save(item);
    }
}
