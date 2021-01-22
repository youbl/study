package beinet.cn.springbeanstudy.scopeBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * SingletonBeanDemo
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/22 10:53
 */
@Service
@Scope("singleton")
public class SingletonBeanDemo {
    private int num;

    public int addNum(int n) {
        num += n;
        return num;
    }
}
