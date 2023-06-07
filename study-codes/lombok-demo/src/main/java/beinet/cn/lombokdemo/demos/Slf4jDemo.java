package beinet.cn.lombokdemo.demos;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
@Slf4j
public class Slf4jDemo {

    public void doLog() {
        log.debug("DEBUG日志：当前时间:{}", LocalDateTime.now());
        log.info("INFO日志：当前时间:{}", LocalDateTime.now());
        log.warn("warn日志：当前时间:{}", LocalDateTime.now(), new Exception("我是异常"));
        log.error("error日志：当前时间:{}", LocalDateTime.now(), new Exception("我是异常"));
    }
}
