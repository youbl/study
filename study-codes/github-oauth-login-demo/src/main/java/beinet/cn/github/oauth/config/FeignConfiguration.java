package beinet.cn.github.oauth.config;

import beinet.cn.github.oauth.config.log.MyFeignLogger;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 开feign日志的配置类
 * @author youbl
 * @since 2025/4/29 17:27
 */
@Configuration
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    MyFeignLogger createMyLogger() {
        return new MyFeignLogger();
    }
}
