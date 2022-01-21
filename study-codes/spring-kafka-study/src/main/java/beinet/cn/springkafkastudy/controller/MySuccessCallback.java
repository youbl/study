package beinet.cn.springkafkastudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/1/21 13:59
 */
@Slf4j
@Component
public class MySuccessCallback implements SuccessCallback {
    @Override
    @Async
    public void onSuccess(Object result) {
        log.info("SuccessCallback发送成功:" + ((SendResult) result).getRecordMetadata().offset());
        sleep(5);
        log.info("SuccessCallback休眠成功");
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception exp) {
            Thread.currentThread().interrupt();
        }
    }
}
