package beinet.cn.springconfigclientgit.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Set;

/**
 * 定时轮询配置中心API，判断并刷新配置的定时任务类
 *
 * @author youbl
 * @since 2023/5/24 15:24
 */
@Component
@ConditionalOnProperty(value = "spring.cloud.config.refresh.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class ConfigsRefresh implements InitializingBean {

    private final FeignConfigServer feignConfigServer;
    private final org.springframework.cloud.context.refresh.ConfigDataContextRefresher refresher;

    @Value("${spring.application.name:}")
    private String appName;

    private LocalDateTime lastUpdateTime = LocalDateTime.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss.SSS");
    private static final Random RANDOM = new Random();

    /**
     * 每分钟检查并刷新
     */
    @Scheduled(cron = "0 * * * * *")
    public void doRefresh() {
        LocalDateTime serverUpdateTime = getServerLastUpdateTime(appName);
        if (serverUpdateTime.compareTo(lastUpdateTime) <= 0) {
            return;
        }
        log.info("本地更新时间较小:{} < {} 需要刷新配置", lastUpdateTime, serverUpdateTime);
        sleep();

        Set<String> keys = refresher.refresh();

        lastUpdateTime = serverUpdateTime;
        log.info("更新列表 {}", keys);
    }

    // 获取当前应用，配置中心的配置最近刷新时间
    private LocalDateTime getServerLastUpdateTime(String appName) {
        try {
            String serverUpdateTime = feignConfigServer.getTime(appName);
            // log.info("配置中心最近更新: {}", serverUpdateTime);
            if (StringUtils.hasLength(serverUpdateTime)) {
                return LocalDateTime.parse(serverUpdateTime, formatter);
            }
        } catch (Exception exp) {
            log.error("取配置中心更新时间出错:", exp);
        }
        return LocalDateTime.MIN;
    }

    private void sleep() {
        // 随机休眠，避免所有pod同时刷新配置
        int selectSecond = RANDOM.nextInt(10);
        try {
            Thread.sleep(selectSecond * 1000L);
        } catch (InterruptedException e) {
            log.error("休眠出错", e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        lastUpdateTime = LocalDateTime.now();
        log.info("配置本地更新时间:{}", lastUpdateTime);
    }
}
