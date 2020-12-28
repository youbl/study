package beinet.cn.springbeanstudy.beans;

import org.springframework.beans.factory.ObjectProvider;

/**
 * BeanDemo1
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 11:43
 */
public class BeanDemo2 {
    private BeanDemo1 demo1;

    public BeanDemo2(ObjectProvider<BeanDemo1> demo1) {
        System.out.println(this.getClass().getName() + " created");
       // this.demo1 = demo1;
    }

    public void method1() {
        System.out.println(this.getClass().getName() + " method1 executed.");
    }
}
