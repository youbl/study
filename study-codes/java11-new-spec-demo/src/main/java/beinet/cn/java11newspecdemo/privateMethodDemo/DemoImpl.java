package beinet.cn.java11newspecdemo.privateMethodDemo;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/7 17:26
 */
public class DemoImpl implements PrivateMethodDemo {
    public int getMine() {
        return get(); // 接口上的default方法
    }
}
