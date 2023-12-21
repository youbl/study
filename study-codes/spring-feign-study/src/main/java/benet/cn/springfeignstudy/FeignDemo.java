package benet.cn.springfeignstudy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FeignDemo
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/7 17:11
 */
@FeignClient(value = "demo", url = "https://www.baidu.com/")
public interface FeignDemo {
    @GetMapping(value = "", headers = {"a=1;b=2", "ab=自动化测试员"})
    String xxx();
}
