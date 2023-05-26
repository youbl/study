package beinet.cn.springconfigclientgit;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/26 10:17
 */
@RestController
public class HomeController {
    @GetMapping("envs")
    public Map<String, Map<String, String>> getAllEnv() {
        Map<String, Map<String, String>> properties = new HashMap<>();

        AbstractEnvironment env = SpringUtils.getBean(AbstractEnvironment.class);
        for (PropertySource<?> propertySource : env.getPropertySources()) {
            Map<String, String> keyVal = new HashMap<>();
            properties.put(propertySource.getName(), keyVal);
            // System.out.println("=== " + propertySource.getName() + " ===");
            if (propertySource instanceof MapPropertySource) {
                enumProperties(propertySource, keyVal);
            } else {//if (propertySource instanceof ConfigurationPropertySourcesPropertySource) {
                System.out.println(propertySource);
            }
        }
        return properties;
    }

    private void enumProperties(PropertySource source, Map<String, String> keyVal) {
        for (String name : ((MapPropertySource) source).getPropertyNames()) {
            keyVal.put(name, source.getProperty(name) + "");
        }
    }
}
