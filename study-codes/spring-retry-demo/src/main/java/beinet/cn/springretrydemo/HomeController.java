package beinet.cn.springretrydemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

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

    // 默认重试3次，间隔1秒，任何异常都触发重试（注：包括第一次请求，一共尝试3次）
    @Retryable
    @GetMapping("retry2")
    public String retry2() {
        idx++;
        log.info("retry2 第" + idx + "次访问，准备抛异常");
        throw new RuntimeException("我是异常");
    }

    private LocalDateTime dt = LocalDateTime.now();
    private int idx3;

    // 任何异常都触发重试，一共请求10次，且每次重试都是上次等待时长的1.5倍
    // 第1次~第2次间隔500ms，第2次~第3次间隔500*1.5=750ms，第3次~第4次间隔500*1.5*1.5=1125ms，如此类推
    @Retryable(value = Exception.class, maxAttempts = 10, backoff = @Backoff(delay = 500, multiplier = 1.5))
    @GetMapping("retry3")
    public String retry3() {
        idx3++;
        LocalDateTime now = LocalDateTime.now();
        System.out.println(Duration.between(dt, now).toMillis());
        dt = now;
        log.info("retry3 第" + idx3 + "次访问，准备抛异常");
        if (idx3 >= 9)
            return "retry3---" + now.toString();
        throw new RuntimeException("我是异常");
    }

    private int idx4;

    // 任何异常都触发重试，一共请求5次，
    // 如果最后一次还是异常，则执行并返回recover方法的值（定义在同一个类里）
    @Retryable(maxAttempts = 5, recover = "myRecover")
    @GetMapping("retry4")
    public String retry4(@RequestParam Integer para1, @RequestParam String para2) {
        idx4++;
        log.info("retry4 第" + idx4 + "次访问，准备抛异常: " + para1 + " " + para2);
        throw new RuntimeException("我是异常");
    }

    // recover方法，可以是private，但是：
    // 1、第一个参数必须是Exception类型(或与retry方法抛出的异常一致类型的参数）
    // 2、其它参数的类型和顺序必须与retry方法一致
    // 3、返回值必须与retry方法一致
    // 4、不支持基本类型参数，如int
    @Recover
    private String myRecover(Exception exp, Integer para, String para2) {
        return para2 + "--" + para + ", myRecover返回了:" + exp.getMessage();
    }
}