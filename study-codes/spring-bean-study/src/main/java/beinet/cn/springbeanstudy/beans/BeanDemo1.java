package beinet.cn.springbeanstudy.beans;

import org.springframework.beans.factory.ObjectProvider;

/**
 * BeanDemo1
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 11:43
 */
public class BeanDemo1 {
    private ObjectProvider<BeanDemo3> demo3;

    public BeanDemo1(ObjectProvider<BeanDemo3> demo3) {
        this.demo3 = demo3;
        System.out.println(this.getClass().getName() + " created");
    }

    public void method1() {
        BeanDemo3 demoReal = demo3.getIfAvailable();
        demoReal.method1();

        System.out.println(this.getClass().getName() + " method1 executed.");
    }
}
