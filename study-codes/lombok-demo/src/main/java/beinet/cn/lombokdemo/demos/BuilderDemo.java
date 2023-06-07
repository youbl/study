package beinet.cn.lombokdemo.demos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuilderDemo {

    private int id;

    private String name;
}
