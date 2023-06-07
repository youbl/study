package beinet.cn.lombokdemo.demos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
@Data
@Accessors(chain = true)
public class AccessorsDemo {

    private int id;

    private String name;
}
