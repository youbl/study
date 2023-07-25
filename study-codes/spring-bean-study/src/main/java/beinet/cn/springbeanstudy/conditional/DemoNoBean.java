package beinet.cn.springbeanstudy.conditional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/7/25 17:15
 */
//@RequiredArgsConstructor
@Component
public class DemoNoBean {
    /* 使用RequiredArgsConstructor时，
    如果DemoStrContain因为Conditional条件不满足，导致Bean未注册，那么这里会抛异常:
    Parameter 0 of constructor in beinet.cn.springbeanstudy.conditional.DemoNoBean
    required a bean of type 'beinet.cn.springbeanstudy.conditional.DemoStrContain' that could not be found.

    使用     @Autowired(required = false) 则不影响启动
    * */
    @Autowired(required = false)
    private DemoStrContain deom;

    @PostConstruct
    public void initedOK() {
        System.out.println("===初始化成功:" + this.getClass().getName());
    }

}
