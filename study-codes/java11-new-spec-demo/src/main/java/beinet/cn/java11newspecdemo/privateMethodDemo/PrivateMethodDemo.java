package beinet.cn.java11newspecdemo.privateMethodDemo;

/**
 * 新接口
 *
 * @author youbl
 * @since 2023/6/7 17:23
 */
public interface PrivateMethodDemo {
    // 允许定义private的成员方法，只能在接口内部的default方法中使用
    private int get123() {
        return 123;
    }

    // 允许定义private的静态方法，只能在接口内部的default方法中使用
    private static int get456() {
        return 123;
    }

    // default的方法只能是public的，所以不需要public声明
    default int get() {
        return get123() + get456();
    }
}
