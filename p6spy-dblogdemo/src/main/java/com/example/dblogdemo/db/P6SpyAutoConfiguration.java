package com.example.dblogdemo.db;


import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyDriver;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.option.SystemProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(P6SpyDriver.class)
@EnableConfigurationProperties(P6SpyProperties.class)
//@ConditionalOnProperty(prefix = P6SpyProperties.P6SPY_PROPERTY_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class P6SpyAutoConfiguration implements ApplicationRunner {
    private final Environment env;

    public P6SpyAutoConfiguration(Environment environment) {
        this.env = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, String> originDefaultVal = P6SpyOptions.getActiveInstance().getDefaults();

        P6SpyProperties defaultVal = new P6SpyProperties();
        Field[] fields = P6SpyProperties.class.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String propertiesName = SystemProperties.P6SPY_PREFIX.concat(fieldName);
            String propertyValue;
            if (env.containsProperty(propertiesName)) {
                propertyValue = env.getProperty(propertiesName, originDefaultVal.get(fieldName));
            } else {
                field.setAccessible(true);
                propertyValue = String.valueOf(field.get(defaultVal));
            }
            originDefaultVal.put(fieldName, propertyValue);
        }
        P6SpyOptions.getActiveInstance().load(originDefaultVal);
        P6ModuleManager.getInstance().reload();
    }

}
