package beinet.cn.lombokdemo.demos;

import lombok.ToString;
import org.springframework.core.env.Environment;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/9 14:55
 */
@ToString
public class ToStringDemo {
    private int id;
    private final String name = "";
    private final Environment env = null;
}

/**
 * 生成代码参考：
 * public class ToStringDemo {
 *     private int id;
 *     private final String name = "";
 *     private final Environment env = null;
 *
 *     public String toString() {
 *         int i = this.id;
 *         Objects.requireNonNull(this);
 *         return "ToStringDemo(id=" + i + ", name=" + "" + ", env=" + this.env + ")";
 *     }
 * }
 */
