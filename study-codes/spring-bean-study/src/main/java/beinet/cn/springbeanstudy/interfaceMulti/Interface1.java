package beinet.cn.springbeanstudy.interfaceMulti;

/**
 * Interface1
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 14:28
 */
public interface Interface1 {

    default void method1() {
        System.out.println(this.getClass().getName() + " method1 ");
    }
}
