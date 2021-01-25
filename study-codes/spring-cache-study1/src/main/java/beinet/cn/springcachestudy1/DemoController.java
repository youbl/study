package beinet.cn.springcachestudy1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * DemoController
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/23 15:12
 */
@RestController
public class DemoController {
    @Autowired
    CacheManager cacheManager;

    @Cacheable(cacheNames = "beinet-${spring.application.name:UNKNOWN}", cacheResolver = "beinetCacheResolver")
    @GetMapping("")
    public String index() {
        return LocalDateTime.now().toString();
    }

    @Cacheable(cacheNames = "beinet-cache1", key = "#id.toString() + '-' + #name")
    @GetMapping("index")
    public String index(@RequestParam int id, @RequestParam String name) {
        return LocalDateTime.now().toString();
    }
}
