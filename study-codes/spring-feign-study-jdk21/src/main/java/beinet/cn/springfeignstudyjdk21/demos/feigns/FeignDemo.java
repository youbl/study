package beinet.cn.springfeignstudyjdk21.demos.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "demo", url = "https://www.baidu.com/")
public interface FeignDemo {
    @GetMapping(value = "", headers = {"a=1;b=2", "ab=自动化测试员"})
    String xxx();
}
