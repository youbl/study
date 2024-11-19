package cn.beinet.core.base.configs;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 读取固定配置的通用方法类
 *
 * @author youbl
 * @since 2024/7/15 20:05
 */
@Component
public class ConfigConst implements ApplicationContextAware {
    /**
     * 本地环境
     */
    public static String LOCAL = "local";
    /**
     * 开发环境
     */
    public static String DEV = "dev";
    /**
     * 测试环境
     */
    public static String TEST = "test";
    /**
     * 预上环境
     */
    public static String PRE = "pre";
    /**
     * 生产环境
     */
    public static String PROD = "prod";
    /**
     * 模拟压测环境
     */
    public static String SIM = "sim";

    @Getter
    private static String activeProfile;
    @Getter
    private static String appName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Environment env = applicationContext.getEnvironment();

        String[] profiles = env.getActiveProfiles();
        activeProfile = profiles.length == 0 ? "" : profiles[0];
        activeProfile = activeProfile == null ? "" : activeProfile;

        // ContextIdApplicationContextInitializer类里直接获取spring.application.name
        // 未配置里，返回 "application"
        appName = applicationContext.getId();
    }

    public static String getEnv() {
        return activeProfile;
    }

    public static boolean isLocal() {
        return activeProfile.equals(LOCAL);
    }

    public static boolean isDev() {
        return activeProfile.equals(DEV);
    }

    public static boolean isTest() {
        return activeProfile.equals(TEST);
    }

    public static boolean isPre() {
        return activeProfile.equals(PRE);
    }

    public static boolean isSim() {
        return activeProfile.equals(SIM);
    }

    public static boolean isProd() {
        return activeProfile.equals(PROD);
    }

    /**
     * 是否运维后台
     *
     * @return 是否
     */
    public static boolean isAdminApp() {
        return "beinet-admin".equals(getAppName());
    }


    /**
     * 是否Business业务站
     *
     * @return 是否
     */
    public static boolean isBusinessApp() {
        return "beinet-business".equals(getAppName());
    }

    /**
     * 是否Job业务站
     *
     * @return 是否
     */
    public static boolean isJobApp() {
        return "beinet-job".equals(getAppName());
    }
}
