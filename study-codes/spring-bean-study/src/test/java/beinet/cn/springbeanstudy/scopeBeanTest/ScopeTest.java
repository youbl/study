package beinet.cn.springbeanstudy.scopeBeanTest;

import beinet.cn.springbeanstudy.scopeBean.DefaultBeanDemo;
import beinet.cn.springbeanstudy.scopeBean.PrototypeBeanDemo;
import beinet.cn.springbeanstudy.scopeBean.SingletonBeanDemo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * ScopeTest
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/22 11:03
 */
@SpringBootTest
public class ScopeTest {
    // 注：不能使用Autowired，因为对于单例，只注入一次，导致prototype也变成单例了
    @Autowired
    PrototypeBeanDemo prototypeBeanDemo;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void test_default_bean() {
        DefaultBeanDemo defaultBeanDemo = applicationContext.getBean(DefaultBeanDemo.class);
        int num = defaultBeanDemo.addNum(1);
        assert num == 1;
        defaultBeanDemo = applicationContext.getBean(DefaultBeanDemo.class);
        num = defaultBeanDemo.addNum(1);
        assert num == 2;
    }

    @Test
    public void test_singleton_bean() {
        SingletonBeanDemo bean = applicationContext.getBean(SingletonBeanDemo.class);
        int num = bean.addNum(1);
        assert num == 1;
        bean = applicationContext.getBean(SingletonBeanDemo.class);
        num = bean.addNum(1);
        assert num == 2;
    }

    @Test
    public void test_prototype_bean() {
        PrototypeBeanDemo bean = applicationContext.getBean(PrototypeBeanDemo.class);
        int num = bean.addNum(1);
        assert num == 1;
        bean = applicationContext.getBean(PrototypeBeanDemo.class);
        num = bean.addNum(1);
        assert num == 1;
    }
}
