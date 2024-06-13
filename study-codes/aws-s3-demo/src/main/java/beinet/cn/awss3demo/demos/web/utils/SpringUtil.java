package beinet.cn.awss3demo.demos.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2024/6/12 20:12
 */
@Component
public class SpringUtil implements ApplicationContextAware {
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

    public static String getActiveProfile() {
        if (null == springApplicationContext) {
            return "";
        }
        String[] activeProfiles = springApplicationContext.getEnvironment().getActiveProfiles();
        return activeProfiles != null && activeProfiles.length > 0 ? activeProfiles[0] : null;
    }

    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    public static String getProperty(String key) {
        if (null == springApplicationContext) {
            return "";
        }
        String ret = springApplicationContext.getEnvironment().getProperty(key);
        return ret == null ? "" : ret;
    }
}
