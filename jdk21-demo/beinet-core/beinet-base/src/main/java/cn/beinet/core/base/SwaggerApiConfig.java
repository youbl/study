package cn.beinet.core.base;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 新类
 * @author youbl
 * @since 2024/11/20 19:11
 */
@Configuration
public class SwaggerApiConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        Info info = new Info()
                .title("beinet.cn jdk21 API demo文档")
                .description("这是水边提供的jdk21代码demo，参考：https://youbl.blog.csdn.net/")
                .version("0.0.1"); // 版本号
        return new OpenAPI().info(info);
    }
}
