package beinet.cn.springretrydemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/8 20:16
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final RetryService retryService;

    @GetMapping("")
    public String index() {
        return this.getClass().getName();
    }

    @GetMapping("retry")
    public String retry() throws Exception {
        log.info("准备抛异常");
        retryService.retry();
        return "aaa";
    }

    private int idx = 0;

    // 默认重试3次，间隔1秒，任何异常
    @Retryable
    @GetMapping("retry2")
    public String retry2() {
        idx++;
        log.info("retry2 第" + idx + "次访问，准备抛异常");
        throw new RuntimeException("我是异常");
    }
}