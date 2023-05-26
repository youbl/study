package beinet.cn.springconfigclientgit.configs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/25 14:31
 */
@FeignClient(name = "config-server", url = "http://localhost:8999")
public interface FeignConfigServer {

    @GetMapping("/lastTime")
    String getTime(@RequestParam String appName);
}
