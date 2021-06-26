package beinet.cn.springjpastudy;

import beinet.cn.springjpastudy.dto.AaaDto;
import beinet.cn.springjpastudy.mapper.AaaConverter;
import beinet.cn.springjpastudy.repository.Aaa;
import beinet.cn.springjpastudy.repository.AaaRepository;
import beinet.cn.springjpastudy.services.AaaServices;
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

    private final AaaServices aaaServices;

    public HomeController(AaaServices aaaServices) {
        this.aaaServices = aaaServices;
    }

    @GetMapping("dbAll")
    public List<Aaa> getAll() {
        return aaaServices.findAll();
    }

    @GetMapping("dbDto")
    public List<AaaDto> getAllDto() {
        List<Aaa> arr = getAll();
        return AaaConverter.INSTANCE.entity2dto(arr);
    }

    @GetMapping("dbin")
    public List<Aaa> getByIds() {
        // 空数组查询，看看会不会出慢查询
        List<Long> arr = new ArrayList<>();
        int idx = 0;
        while (true) {
            aaaServices.findAllByIdIn(arr);
            idx++;
            if (idx > 10000) break;
        }
        return aaaServices.findAllByIdIn(arr);
    }


    @GetMapping("dbin2")
    public List<Aaa> getByIds2() {
        // 空数组查询，看看会不会出慢查询
        List<Long> arr = new ArrayList<>();
        int idx = 0;
        while (true) {
            aaaServices.findAllByIdIn2(arr);
            idx++;
            if (idx > 10000) break;
        }
        return aaaServices.findAllByIdIn2(arr);
    }

    @GetMapping("db")
    public Aaa getAll(@RequestParam long id) {
        return aaaServices.findById(id).orElse(null);
    }

    @GetMapping("dbupdate")
    public Aaa update(@RequestParam long id, @RequestParam(required = false) int num) {
        Aaa item = aaaServices.findById(id).orElse(null);
        if (item == null) {
            item = new Aaa();
        }
        item.setNum(num);
        item.setDishhour(item.getDishhour() + 1);

        // 先执行: org.springframework.data.jpa.repository.support.SimpleJpaRepository.save 方法
        // 然后是: org.hibernate.internal.firePersist 方法
        // 然后是: org.hibernate.event.internal.AbstractSaveEventListener.performSaveOrReplicate 方法
        /*
        AbstractEntityInsertAction insert = addInsertAction(
				values, id, entity, persister, useIdentityColumn, source, shouldDelayIdentityInserts
		);
        * */
        aaaServices.save(item);
        // save时，会自动给item赋值，因此不需要 item = repostory.save(item); 也可以拿到item.id
        return item;
    }

    @GetMapping("del")
    @Transactional
    public int del() {
        Aaa item = new Aaa();
        item.setNum(123);
        item.setDishhour(444);
        aaaServices.save(item);

        List<Long> ids = new ArrayList<>();
        ids.add(item.getId());
        return aaaServices.deleteByIdIn(ids);
    }

}
