package beinet.cn.springconfigclientgit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2021/11/30 15:09
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext springApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springApplicationContext = applicationContext;
    }


    public static ApplicationContext getApplicationContext() {
        return springApplicationContext;
    }


    /**
     * get bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return springApplicationContext.getBean(name);
    }

    /**
     * get bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return springApplicationContext.getBean(clazz);
    }


    /**
     * get bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return springApplicationContext.getBean(name, clazz);
    }

    /**
     * get getBeansOfType
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return springApplicationContext.getBeansOfType(clazz);
    }

}
