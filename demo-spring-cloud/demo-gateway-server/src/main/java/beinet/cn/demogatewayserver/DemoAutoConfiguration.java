package beinet.cn.demogatewayserver;

import beinet.cn.demogatewayserver.rateLimiter.MyRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class DemoAutoConfiguration {
    @Bean
    public KeyResolver myKeyResolver() {
        return exchange -> {
            return Mono.just(exchange.getRequest().getPath().value());
        };
    }

    @Bean
    public RateLimiter myRateLimiter(ConfigurationService service) {
        return new MyRateLimiter(service);
    }
}
