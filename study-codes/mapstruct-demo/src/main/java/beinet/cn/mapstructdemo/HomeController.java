package beinet.cn.mapstructdemo;

import beinet.cn.mapstructdemo.dto.AaaDto;
import beinet.cn.mapstructdemo.dto.BbbDto;
import beinet.cn.mapstructdemo.entity.Aaa;
import beinet.cn.mapstructdemo.entity.Bbb;
import beinet.cn.mapstructdemo.entityMapper.EntityMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/8 20:16
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String index() {
        return this.getClass().getName();
    }


    @GetMapping("map1")
    public List<AaaDto> get1() {
        List<Aaa> result = init();
        List<AaaDto> ret = EntityMapper.INSTANCE.entity2dto(result);
        return ret;
    }

    @GetMapping("map2")
    public List<Aaa> get2() {
        List<AaaDto> ret = get1();
        return EntityMapper.INSTANCE.dto2entity(ret);
    }

    @GetMapping("map3")
    public List<BbbDto> get3() {
        List<Bbb> ret = init2();
        return EntityMapper.INSTANCE.entity2dto2(ret);
    }

    @GetMapping("map4")
    public List<Bbb> get4() {
        List<BbbDto> ret = get3();
        return EntityMapper.INSTANCE.dto2entity2(ret);
    }

    private List<Aaa> init() {
        List<Aaa> arr = new ArrayList<>();
        arr.add(Aaa.builder()
                .id(11)
                .age(456)
                .name("beinet.cn1")
                .birthday(LocalDateTime.now().minusDays(1))
                .num(1234567L)
                .description("我是描述1")
                .build());
        arr.add(Aaa.builder()
                .id(22)
                .name("beinet.cn2")
                .birthday(LocalDateTime.now().minusDays(1))
                .num(1234567L)
                .description("我是描述2")
                .build());
        arr.add(Aaa.builder()
                .id(33)
                .age(789)
                .name("beinet.cn3")
                .birthday(LocalDateTime.now().minusDays(1))
                .description("我是描述3")
                .build());

        return arr;
    }


    private List<Bbb> init2() {
        List<Bbb> arr = new ArrayList<>();
        arr.add(Bbb.builder()
                .id2(11)
                .age2(456)
                .name2("beinet.cn1")
                .birthday2(LocalDateTime.now().minusDays(1))
                .num2(1234567L)
                .description2("我是描述1")
                .build());
        arr.add(Bbb.builder()
                .id2(22)
                .name2("beinet.cn2")
                .birthday2(LocalDateTime.now().minusDays(1))
                .num2(1234567L)
                .description2("我是描述2")
                .build());
        arr.add(Bbb.builder()
                .id2(33)
                .age2(789)
                .name2("beinet.cn3")
                .birthday2(LocalDateTime.now().minusDays(1))
                .description2("我是描述3")
                .build());

        return arr;
    }
}
/*
    private int id;
    private Integer age;
    private String name;
    private LocalDateTime birthday;

    private Long num;

    private String description;
* */
