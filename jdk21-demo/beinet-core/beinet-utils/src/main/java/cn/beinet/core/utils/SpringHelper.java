package cn.beinet.core.utils;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring bean辅助类
 *
 * @author youbl
 * @since 2021/11/30 15:09
 */
@Component
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext springApplicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        springApplicationContext = applicationContext;
    }


    public static ApplicationContext getApplicationContext() {
        return springApplicationContext;
    }


    /**
     * get bean
     *
     * @param name bean name
     * @return bean
     */
    public static Object getBean(String name) {
        return springApplicationContext.getBean(name);
    }

    /**
     * get bean
     *
     * @param clazz bean type
     * @param <T>  bean type
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return springApplicationContext.getBean(clazz);
    }


    /**
     * get bean
     *
     * @param name bean name
     * @param clazz  bean type
     * @param <T>  bean type
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return springApplicationContext.getBean(name, clazz);
    }

    /**
     * get getBeansOfType
     *
     * @param clazz  bean type
     * @param <T>  bean type
     * @return bean
     */
    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return springApplicationContext.getBeansOfType(clazz);
    }
}
