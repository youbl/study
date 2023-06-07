package beinet.cn.lombokdemo.demos;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
@Slf4j
public class SynchronizedDemo {
    private static int num;

    @Synchronized
    @SneakyThrows
    public void syncMethod() {
        num++;
        log.info("before-num:{}", num);

        Thread.sleep(3000);

        num++;
        log.info("after-num:{}", num);
    }

    @Synchronized
    @SneakyThrows
    public static void syncStaticMethod() {
        num++;
        log.info("before-num:{}", num);

        Thread.sleep(3000);

        num++;
        log.info("after-num:{}", num);
    }
}
