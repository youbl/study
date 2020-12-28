package beinet.cn.springbeanstudy.beans;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MyInterceptor
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 12:57
 */
public class MyInterceptor implements HandlerInterceptor {
    BeanDemo3 beanDemo3;

    public MyInterceptor(BeanDemo3 beanDemo3) {
        this.beanDemo3 = beanDemo3;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        beanDemo3.method1();
        return true;
    }
}
