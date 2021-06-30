package beinet.cn.springbeanstudy.interfaceMulti;

import org.springframework.core.Ordered;

/**
 * Implment1
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 14:30
 */
public class Implment13 implements Interface1, Ordered {
    // 用于Autowired注入的List里的顺序，值越小越靠前，未实现Ordered接口的排在最后
    // 访问 http://localhost:8801/multiInterface 进行验证
    @Override
    public int getOrder() {
        return -2;
    }
}
