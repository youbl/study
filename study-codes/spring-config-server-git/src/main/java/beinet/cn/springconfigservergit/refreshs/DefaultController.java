package beinet.cn.springconfigservergit.refreshs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/22 17:58
 */
@RestController
public class DefaultController {
    private final RefreshService refreshService;

    public DefaultController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    /**
     * 查看全局和所有应用的最近配置更新时间
     *
     * @return 时间
     */
    @GetMapping("lastAllTime")
    public Map<String, String> getAllTime() {
        return refreshService.getAllTime();
    }

    /**
     * 查看指定应用的的最近配置更新时间
     *
     * @param appName 应用
     * @return 时间
     */
    @GetMapping("lastTime")
    public String getTime(@RequestParam(required = false) String appName) {
        return refreshService.getLastUpdateTime(appName);
    }

    /**
     * 修改指定应用的最近配置更新时间为now
     *
     * @param appName 应用
     * @return 更新后时间
     */
    @PostMapping("lastTime")
    public String setTime(@RequestParam(required = false) String appName) {
        refreshService.setLastUpdateTime(appName, LocalDateTime.now());
        return getTime(appName);
    }

    /**
     * 修改所有应用的最近配置更新时间为now
     *
     * @return 更新后时间
     */
    @PostMapping("lastAllTime")
    public String setGlobalTime() {
        refreshService.setGlobalLastUpdateTime(LocalDateTime.now());
        return getTime(null);
    }
}
