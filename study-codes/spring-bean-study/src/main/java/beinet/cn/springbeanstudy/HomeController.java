package beinet.cn.springbeanstudy;

import beinet.cn.springbeanstudy.beans.BeanDemo1;
import beinet.cn.springbeanstudy.interfaceMulti.UsingAll;
import beinet.cn.springbeanstudy.multiBean.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 11:21
 */
@RestController
public class HomeController {
    @Autowired
    BeanDemo1 demo1;

    @Autowired
    ServiceConfig.ServiceCls AServiceCls;

    @Autowired
    ServiceConfig.ServiceCls BServiceCls;

    // 上面2个不会报错，自动根据变量名查找匹配的Bean
    // 这个会报错： Field bServiceCls in beinet.cn.springbeanstudy.HomeController required a single bean, but 2 were found:
//    @Autowired
//    ServiceConfig.ServiceCls bServiceCls;

    @GetMapping("multi")
    public void multi() {
        AServiceCls.run();
        BServiceCls.run();
    }

    @GetMapping("")
    public String index() {
        demo1.method1();
        return this.getClass().getName();
    }


    @Autowired
    UsingAll usingAll;

    @GetMapping("multiInterface")
    public String multiInterface() {
        usingAll.execute();
        return this.getClass().getName();
    }

    @GetMapping("exp1")
    public int exp1(@RequestParam(required = false) Integer num) {
        return 100 / num;// num为空或0会抛异常
    }

    @GetMapping("exp2")
    public int exp2(@RequestParam(required = false) Integer num) throws ClassNotFoundException {
        if (num == null) {
            throw new RuntimeException("num不能为空");
        }
        if (num.intValue() == 0) {
            throw new IllegalArgumentException("num不能为0");
        }
        if (num < 0) {
            throw new ClassNotFoundException("要触发Exception方法");
        }
        return 100 / num;// num为空或0会抛异常
    }
}
