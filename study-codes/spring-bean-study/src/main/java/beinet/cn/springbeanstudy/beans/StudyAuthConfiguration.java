package beinet.cn.springbeanstudy.beans;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * StudyAuthConfiguration
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 11:40
 */
@Configuration
public class StudyAuthConfiguration {
//    @Autowired
//    BeanDemo3 demo3;

    // ObjectProvider 可以避免循环依赖，实现后加载
    @Bean
    public BeanDemo1 createBeanDemo1(ObjectProvider<BeanDemo3> demo3) {
        return new BeanDemo1(demo3);
    }

    @Bean
    public BeanDemo2 createBeanDemo2(ObjectProvider<BeanDemo1> demo1) {
        return new BeanDemo2(demo1);
    }

    @Bean
    public BeanDemo3 createBeanDemo3(ObjectProvider<BeanDemo2> demo2) {
        return new BeanDemo3(demo2);
    }

    @Configuration
    static class MyMvcConfig implements WebMvcConfigurer {
        @Autowired
        BeanDemo3 beanDemo3;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new MyInterceptor(beanDemo3));
        }
    }
}
