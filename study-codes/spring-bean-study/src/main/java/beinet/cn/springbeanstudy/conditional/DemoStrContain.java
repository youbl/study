package beinet.cn.springbeanstudy.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/7/25 14:01
 */
@Component
// 配置了myapp.str,且该字符串包含bbb时，才会注册这个Bean，未配置则不会注册
@ConditionalOnExpression(value = "#{environment['myapp.str']!=null &&environment['myapp.str'].contains('bbb')}")
public class DemoStrContain {
    public DemoStrContain() {
        System.out.println("===初始化成功:" + this.getClass().getName());
    }
}
