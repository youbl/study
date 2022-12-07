package com.example.threaddemo.poolDemo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 新类
 *
 * @author youbl
 * @date 2022/12/6 19:23
 */
@Data
@Slf4j
public class MyRunnable implements Runnable {
    private String name;
    private int second;

    public MyRunnable(String name, int second) {
        this.name = name;
        this.second = second;
    }

    @Override
    public void run() {
        sleep();
        log.info(name + "运行了，休眠了" + second + "秒");
    }

    private void sleep() {
        try {
            if (second > 0) {
                Thread.sleep(second * 1000);
            }
        } catch (Exception exp) {
            log.error(exp.toString());
        }
    }
}
