package beinet.cn.demologfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "testBaidu", url = "https://www.baidu.com/")
public interface FeignBaidu {
    @GetMapping("")
    String getHome();
}
