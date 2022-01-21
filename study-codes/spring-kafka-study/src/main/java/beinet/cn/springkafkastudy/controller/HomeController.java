package beinet.cn.springkafkastudy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:43
 */
@RestController
@Slf4j
// @AllArgsConstructor 不支持String的topic字段
@RequiredArgsConstructor
public class HomeController {
    private final KafkaTemplate kafkaTemplate;
    private final SuccessCallback successCallback;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    @GetMapping("")
    public String index() {
        return LocalDateTime.now() + " " + this.getClass().getName();
    }

    // 同步发消息到kafka
    @GetMapping("send")
    public String sendMsg(@RequestParam String msg) throws ExecutionException, InterruptedException {
        if (!StringUtils.hasLength(msg)) {
            return "要发的数据不能为空";
        }

        Map<String, String> map = getMap(msg);
        // 投递，参数1为主题，参数2为key（选分区用），参数3为数据（序列化类在配置里）
        // 默认情况下，kafka的消息体大小不能超过1M，否则会推送失败
        // 可以自己序列化map为String后，判断大小不超1M再投递
        Object result = kafkaTemplate.send(topic, map.get("time"), map).get();

        if (result instanceof SendResult) {
            SendResult sendResult = (SendResult) result;
            return String.format("同步发送成功,偏移量:%s, 投递分区:%s, 时间戳:%s",
                    sendResult.getRecordMetadata().offset(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().timestamp());
        }
        return String.format("同步发送成功,结果不是result:%s", result);//.getRecordMetadata().offset());
    }

    // 异步回调方式发消息到kafka，使用addCallback机制
    @GetMapping("sendAsync1")
    public String sendMsgAsync1(@RequestParam String msg, @RequestParam(required = false, defaultValue = "10") int cnt) {
        Map<String, String> map = getMap(msg);

        // 发送n次
        for (int i = 0; i < cnt; i++) {
            ListenableFuture<SendResult> future = kafkaTemplate.send(topic, map.get("time"), map);
            future.addCallback(success -> {
                        log.info("异步1发送成功:" + success.getRecordMetadata().offset());
                        sleep(5);
                        log.info("异步1休眠成功");
                    },
                    fail -> log.error("异步1发送失败:" + fail.getCause()));
        }
        return "异步1发送成功";
    }

    // 异步回调方式发消息到kafka，使用addCallback机制
    @GetMapping("sendAsync2")
    public String sendMsgAsync2(@RequestParam String msg, @RequestParam(required = false, defaultValue = "10") int cnt) {
        Map<String, String> map = getMap(msg);

        // 发送n次
        for (int i = 0; i < cnt; i++) {
            ListenableFuture<SendResult> future = kafkaTemplate.send(topic, map.get("time"), map);
            future.addCallback(successCallback, fail -> log.error("异步2发送失败:" + fail.getCause()));
        }
        return "异步2发送成功";
    }

    // 异步回调方式发消息到kafka，使用producerListener机制
    // 无异步效果，所有onSuccess还是同步执行
    @GetMapping("sendAsync3")
    public String sendMsgAsync3(@RequestParam String msg, @RequestParam(required = false, defaultValue = "10") int cnt) {
        Map<String, String> map = getMap(msg);
        // new出来的对象，Async注解无效
        kafkaTemplate.setProducerListener(new MyProducerListener());

        for (int i = 0; i < cnt; i++) {
            kafkaTemplate.send(topic, map.get("time"), map);
        }
        return String.format("异步2发送成功");
    }


    private Map<String, String> getMap(String msg) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        map.put("time", now);
        map.put("class", this.getClass().getName());
        return map;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception exp) {
            Thread.currentThread().interrupt();
        }
    }
}
