package beinet.cn.demologfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "testBaidu", url = "https://www.baidu.com/")
public interface FeignBaidu {
    @PostMapping(value = "", headers = {"aaa=bbb", "ccc=ddd"})
    String getHome(Map paras);
}
