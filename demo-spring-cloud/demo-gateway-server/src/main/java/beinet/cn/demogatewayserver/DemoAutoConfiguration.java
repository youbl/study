package beinet.cn.demogatewayserver;

import beinet.cn.demogatewayserver.rateLimiter.MyRateLimiter;
import beinet.cn.demogatewayserver.rateLimiter.AbcRequestRateLimiterGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Value;
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
    public RateLimiter myRateLimiter(ConfigurationService service,
                                     @Value("${beinet.url}") String limitedUrl) {
        return new MyRateLimiter(service, limitedUrl);
    }

    @Bean
    public AbcRequestRateLimiterGatewayFilterFactory myRequestRateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        return new AbcRequestRateLimiterGatewayFilterFactory(defaultRateLimiter, defaultKeyResolver);
    }
}
