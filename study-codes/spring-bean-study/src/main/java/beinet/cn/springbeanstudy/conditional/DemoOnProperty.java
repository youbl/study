package beinet.cn.springbeanstudy.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/7/25 14:01
 */
@Component
// 配置了myapp.demo-config时，才会注册这个Bean，未配置则不会注册
@ConditionalOnProperty(value = "myapp.demo-config", matchIfMissing = false)
public class DemoOnProperty {
    public DemoOnProperty() {
        System.out.println("===初始化成功:" + this.getClass().getName());
    }
}
