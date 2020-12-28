package beinet.cn.springbeanstudy;

import beinet.cn.springbeanstudy.beans.BeanDemo1;
import beinet.cn.springbeanstudy.interfaceMulti.UsingAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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


}
