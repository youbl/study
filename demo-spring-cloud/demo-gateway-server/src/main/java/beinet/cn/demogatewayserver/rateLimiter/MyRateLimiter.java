package beinet.cn.demogatewayserver.rateLimiter;

import io.github.bucket4j.*;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyRateLimiter extends AbstractRateLimiter<MyRateLimiter.Config> {
    public static final String CONFIGURATION_PROPERTY_NAME = "in-memory-rate-limiter";
    private Config defaultConfig;

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    public MyRateLimiter(ConfigurationService service) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, service);
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

        // How many requests per second do you want a user to be allowed to do?
        int replenishRate = routeConfig.getReplenishRate();

        // How much bursting do you want to allow?
        int burstCapacity = routeConfig.getBurstCapacity();

        // init
        Bucket bucket = ipBucketMap.computeIfAbsent(id, k -> {
            Refill refill = Refill.of(replenishRate, Duration.ofSeconds(1));
            Bandwidth limit = Bandwidth.classic(burstCapacity, refill);
            return Bucket4j.builder().addLimit(limit).build();
        });

        Map<String, String> headers = new HashMap<>();
        // tryConsume returns false immediately if no tokens available with the bucket
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        headers.put("remaining", String.valueOf(probe.getRemainingTokens()));
        if (probe.isConsumed()) {
            // the limit is not exceeded
            // probe.getRemainingTokens()
            return Mono.just(new Response(true, headers));
        } else {
            // limit is exceeded
            return Mono.just(new Response(false, headers));
        }
    }

    @Validated
    public static class Config {
        @Min(1)
        private int replenishRate;

        @Min(0)
        private int burstCapacity = 0;

        public int getReplenishRate() {
            return replenishRate;
        }

        public Config setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
            return this;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public Config setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "replenishRate=" + replenishRate +
                    ", burstCapacity=" + burstCapacity +
                    '}';
        }
    }

}
