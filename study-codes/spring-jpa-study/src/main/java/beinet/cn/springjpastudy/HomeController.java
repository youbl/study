package beinet.cn.springjpastudy;

import beinet.cn.springjpastudy.repository.Aaa;
import beinet.cn.springjpastudy.repository.AaaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:43
 */
@RestController
public class HomeController {

    private final AaaRepository repostory;

    public HomeController(AaaRepository repostory) {
        this.repostory = repostory;
    }

    @GetMapping("dbAll")
    public List<Aaa> getAll() {
        return repostory.findAll();
    }


    @GetMapping("dbin")
    public List<Aaa> getByIds() {
        // 空数组查询，看看会不会出慢查询
        List<Long> arr = new ArrayList<>();
        int idx = 0;
        while (true) {
            repostory.findAllByIdIn(arr);
            idx++;
            if (idx > 10000) break;
        }
        return repostory.findAllByIdIn(arr);
    }


    @GetMapping("dbin2")
    public List<Aaa> getByIds2() {
        // 空数组查询，看看会不会出慢查询
        List<Long> arr = new ArrayList<>();
        int idx = 0;
        while (true) {
            repostory.findAllByIdIn2(arr);
            idx++;
            if (idx > 10000) break;
        }
        return repostory.findAllByIdIn2(arr);
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
        }
        item.setNum(num);
        item.setDishhour(item.getDishhour() + 1);
        repostory.save(item);
        // save时，会自动给item赋值，因此不需要 item = repostory.save(item); 也可以拿到item.id
        return item;
    }

    @GetMapping("del")
    @Transactional
    public int del() {
        Aaa item = new Aaa();
        item.setNum(123);
        item.setDishhour(444);
        repostory.save(item);

        List<Long> ids = new ArrayList<>();
        ids.add(item.getId());
        return repostory.deleteByIdIn(ids);
    }

}
