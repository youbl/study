package beinet.cn.springbeanstudy.beans;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * Controller执行前的调用，可以在这里添加或初始化ThreadLocal数据
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        beanDemo3.method1();
        System.out.printf("无论有没有异常，都会执行preHandle\n");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.printf("注意，Controller抛异常时，不会执行postHandle\n");
    }

    /**
     * Controller执行完成后的调用，可以在这里清理ThreadLocal数据之类
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.printf("无论有没有异常，都会执行afterCompletion\n");
    }
}
