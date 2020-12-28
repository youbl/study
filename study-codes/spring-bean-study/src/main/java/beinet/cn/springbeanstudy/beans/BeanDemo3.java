package beinet.cn.springbeanstudy.beans;

import org.springframework.beans.factory.ObjectProvider;

/**
 * BeanDemo1
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 11:43
 */
public class BeanDemo3 {
    private BeanDemo2 demo2;

    public BeanDemo3(ObjectProvider<BeanDemo2> demo2) {
        System.out.println(this.getClass().getName() + " created");
//        this.demo2 = demo2;
    }

    public void method1() {
        System.out.println(this.getClass().getName() + " method1 executed.");
    }
}
