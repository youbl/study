package beinet.cn.springkafkastudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.scheduling.annotation.Async;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/1/21 11:59
 */
@Slf4j
//@Component // 实现ProducerListener接口的Bean会自动注入到KafkaTemplate里
public class MyProducerListener implements ProducerListener {
    @Async
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("Listener发送成功:" + recordMetadata.offset());
        sleep(5);
        log.info("Listener休眠成功");
    }

    @Async
    @Override
    public void onError(ProducerRecord producerRecord, Exception exception) {
        log.error("Listener发送失败:" + exception);
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception exp) {
            Thread.currentThread().interrupt();
        }
    }

}
