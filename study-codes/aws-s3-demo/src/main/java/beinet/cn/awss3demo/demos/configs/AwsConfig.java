package beinet.cn.awss3demo.demos.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 从yml中读取配置的类
 * @author youbl
 * @since 2024/8/27 19:51
 */
@Configuration
@ConfigurationProperties(prefix = "aws-config")
@Data
public class AwsConfig {
    private S3Property S3;
}
