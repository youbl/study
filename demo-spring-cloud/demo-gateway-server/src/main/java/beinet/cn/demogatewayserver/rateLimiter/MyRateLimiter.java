package beinet.cn.demogatewayserver.rateLimiter;

import io.github.bucket4j.*;
import lombok.Data;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyRateLimiter extends AbstractRateLimiter<MyRateLimiter.Config> {
    public static final String CONFIGURATION_PROPERTY_NAME = "in-memory-rate-limiter";
    private final Config defaultConfig;

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();
    private final String limitedUrl;

    public MyRateLimiter(ConfigurationService service,
                         String limitedUrl) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, service);
        this.limitedUrl = limitedUrl;

        defaultConfig = new Config();
        defaultConfig.setReplenishRate(10);
        defaultConfig.setBurstCapacity(100);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        Config routeConfig = getConfig().get(routeId);

        if (routeConfig == null) {
            if (defaultConfig == null) {
                throw new IllegalArgumentException("No Configuration found for route " + routeId);
            }
            routeConfig = defaultConfig;
        }

        // 每秒生成多少个令牌，就是当令牌桶为空时，每秒最多允许多少个用户进入，比如10
        int replenishRate = routeConfig.getReplenishRate();

        // 令牌桶的最大容量，就是突发涌入大量请求时，最多允许多少用户进入，比如100
        int burstCapacity = routeConfig.getBurstCapacity();

        // 初始化当前id的桶
        Bucket bucket = ipBucketMap.computeIfAbsent(id, k -> {
            Refill refill = Refill.of(replenishRate, Duration.ofSeconds(1));
            Bandwidth limit = Bandwidth.classic(burstCapacity, refill);
            return Bucket4j.builder().addLimit(limit).build();
        });

        Map<String, String> headers = new HashMap<>();
        // 尝试获取，同时得到剩余令牌数
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        // 把剩余令牌数写入Header
        headers.put("remaining", String.valueOf(probe.getRemainingTokens()));
        if (probe.isConsumed()) {
            // 拿到令牌，允许进入
            return Mono.just(new Response(true, headers));
        } else {
            // 没令牌了，默认返回429，不允许进入
            headers.put("Location", limitedUrl);
            return Mono.just(new Response(false, headers));
        }
    }

    @Data
    public static class Config {
        private int replenishRate;
        private int burstCapacity;
    }

}
