package beinet.cn.lombokdemo.demos;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/9 14:55
 */
@RequiredArgsConstructor
public class RequiredArgsConstructorDemo {
    private int id;
    private final String name;
    private final Environment env;
}

/**
生成代码参考：
 *public class RequiredArgsConstructorDemo {
 *     private int id;
 *     private final String name;
 *     private final Environment env;
 *
 *     public RequiredArgsConstructorDemo(final String name, final Environment env) {
 *         this.name = name;
 *         this.env = env;
 *     }
 * }
 */
