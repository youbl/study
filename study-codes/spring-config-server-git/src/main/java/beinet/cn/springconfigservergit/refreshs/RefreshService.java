package beinet.cn.springconfigservergit.refreshs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/24 13:40
 */
@Service
public class RefreshService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(RefreshService.class);

    private LocalDateTime globalLastUpdateTime = LocalDateTime.now();

    private Map<String, LocalDateTime> mapAppLastUpdateTimes = new HashMap<>();

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss.SSS");

    /**
     * 返回所有的最近更新时间
     *
     * @return 时间
     */
    public Map<String, String> getAllTime() {
        Map<String, String> ret = new HashMap<>();
        ret.put("全局", globalLastUpdateTime.format(formatter));
        for (Map.Entry<String, LocalDateTime> item : mapAppLastUpdateTimes.entrySet()) {
            ret.put(item.getKey(), item.getValue().format(formatter));
        }
        return ret;
    }

    /**
     * 获取指定app的配置更新时间
     *
     * @param appName 应用名
     * @return 时间
     */
    public String getLastUpdateTime(String appName) {
        appName = formatAppName(appName);

        LocalDateTime time = null;
        if (StringUtils.hasLength(appName)) {
            time = mapAppLastUpdateTimes.get(appName);
        }
        if (time == null) {
            time = globalLastUpdateTime;
        }
        return time.format(formatter);
    }

    /**
     * 设置指定app的配置更新时间，以触发该app更新配置
     *
     * @param appName 应用名
     * @param time    时间
     */
    public void setLastUpdateTime(String appName, LocalDateTime time) {
        appName = formatAppName(appName);

        if (!StringUtils.hasLength(appName))
            throw new RuntimeException("应用名不能为空");
        if (time == null)
            time = LocalDateTime.now();

        mapAppLastUpdateTimes.put(appName, time);
        log.info("{}最后配置更新时间修改为:{}", appName, time);
    }

    /**
     * 设置全局配置更新时间，触发所有app的配置更新
     *
     * @param time 时间
     */
    public void setGlobalLastUpdateTime(LocalDateTime time) {
        if (time == null)
            time = LocalDateTime.now();
        globalLastUpdateTime = time;
        mapAppLastUpdateTimes.clear();
        log.info("全局最后配置更新时间修改为:{}", time);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //globalLastUpdateTime = LocalDateTime.now();
        log.info("启动完成，全局最后配置更新时间为:{}", globalLastUpdateTime);
    }

    private String formatAppName(String appName) {
        if (appName == null)
            return "";
        return appName.trim().toLowerCase();
    }
}
