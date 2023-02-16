package beinet.cn.frontstudy;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 新类
 *
 * @author youbl
 * @date 2023/2/16 17:47
 */
@Component
public final class MyMetrics {
    // 用于单调递增的计数器，比如访问量
    @Bean
    public Counter createCounter(CollectorRegistry collectorRegistry) {
        return Counter.build()
                .name("my_custom_counter")
                .labelNames("first_counter", "test_counter") // 定义几个label，后面在埋点时，就必须传入几个label值
                .help("my test counter")    // 必须设置，不然初始化会报错："Help hasn't been set."
                .register(collectorRegistry); // 注意，一定要注册到 collectorRegistry，否则在/actuator/prometheus看不到数据
    }

    // 用于记录实时数据，如当前CPU的百分比，已用内存字节数等
    @Bean
    public Gauge createGauge(CollectorRegistry collectorRegistry) {
        return Gauge.build()
                .name("my_custom_gauge")
                .labelNames("first_gauge")
                .help("my test gauge")
                .register(collectorRegistry);
    }

    // 用户计算时长，比如方法的执行耗时
    @Bean
    public Summary createSummary(CollectorRegistry collectorRegistry) {
        return Summary.build()
                .name("my_custom_summary")
                .quantile(0.5, 0.05)    // 50分位
                .quantile(0.95, 0.01)   // 95分位
                .labelNames("first_summary")
                .help("my test summary")
                .register(collectorRegistry);
    }
}
