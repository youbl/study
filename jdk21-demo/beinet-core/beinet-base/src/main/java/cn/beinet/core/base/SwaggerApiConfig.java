package cn.beinet.core.base;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 新类
 * @author youbl
 * @since 2024/11/20 19:11
 */
@Configuration
public class SwaggerApiConfig {
    // 参考官网：https://springdoc.org/
    @Bean
    public OpenAPI springOpenAPI() {
        Info info = new Info()
                .title("beinet.cn jdk21 API demo文档")
                .description("这是水边提供的jdk21代码demo，参考：https://youbl.blog.csdn.net/")
                .version("0.0.1") // 版本号
                .license(new License().name("Apache 2.0").url("https://youbl.blog.csdn.net/"));
        ExternalDocumentation doc = new ExternalDocumentation()
                .description("水边的Blog文档")
                .url("https://youbl.blog.csdn.net/");
        return new OpenAPI().info(info)
                .externalDocs(doc);
    }
}
