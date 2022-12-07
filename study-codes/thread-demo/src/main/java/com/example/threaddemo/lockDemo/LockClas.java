package com.example.threaddemo.lockDemo;

import lombok.extern.slf4j.Slf4j;

/**
 * 新类
 *
 * @author youbl
 * @date 2022/12/7 17:30
 */
@Slf4j
public class LockClas {
    private static Object lockObj = new Object();
/*
运行结果如下：
结论：
- wait()方法会阻塞当前方法，并释放拥有的锁，
但是，wait在恢复时，会重新等待并加锁。
- Thread.sleep() 会阻塞当前方法，且不会释放锁
2022-12-07 17:51:04.773  INFO 12776 --- [       Thread-6] c.example.threaddemo.lockDemo.LockClas   : LockClas1加锁成功
2022-12-07 17:51:05.773  INFO 12776 --- [       Thread-7] c.example.threaddemo.lockDemo.LockClas   : LockClas2加锁成功
2022-12-07 17:51:15.779  INFO 12776 --- [       Thread-7] c.example.threaddemo.lockDemo.LockClas   : LockClas2 sleep结束
2022-12-07 17:51:15.780  INFO 12776 --- [       Thread-7] c.example.threaddemo.lockDemo.LockClas   : LockClas2退出加锁
2022-12-07 17:51:15.780  INFO 12776 --- [       Thread-6] c.example.threaddemo.lockDemo.LockClas   : LockClas1 wait结束
2022-12-07 17:51:15.780  INFO 12776 --- [       Thread-6] c.example.threaddemo.lockDemo.LockClas   : LockClas1 退出加锁
* */
    public static class LockClas1 implements Runnable {
        @Override
        public void run() {
            try {
                synchronized (lockObj) {
                    log.info("LockClas1加锁成功");
                    Thread.sleep(1000);
                    lockObj.wait(5000);
                    log.info("LockClas1 wait结束");
                }
                log.info("LockClas1 退出加锁");
            } catch (Exception exp) {
                log.error(exp.toString());
            }
        }
    }

    public static class LockClas2 implements Runnable {
        @Override
        public void run() {
            try {
                synchronized (lockObj) {
                    log.info("LockClas2加锁成功");
                    Thread.sleep(10000);
                    log.info("LockClas2 sleep结束");
                }
                log.info("LockClas2退出加锁");
            } catch (Exception exp) {
                log.error(exp.toString());
            }
        }
    }
}
