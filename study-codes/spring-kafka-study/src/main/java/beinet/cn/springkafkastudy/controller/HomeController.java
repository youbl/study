package beinet.cn.springkafkastudy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.StringUtils;
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
// @AllArgsConstructor 不支持String的topic字段
@RequiredArgsConstructor
public class HomeController {
    private final KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    @GetMapping("")
    public String index() {
        return LocalDateTime.now() + " " + this.getClass().getName();
    }

    @GetMapping("send")
    public String sendMsg(@RequestParam String msg) throws JsonProcessingException, ExecutionException, InterruptedException {
        if (!StringUtils.hasLength(msg)) {
            return "要发的数据不能为空";
        }
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        map.put("time", now);
        map.put("class", this.getClass().getName());

        // 投递，参数1为主题，参数2为key（选分区用），参数3为数据（序列化类在配置里）
        Object result = kafkaTemplate.send(topic, now, map).get();
        if (result instanceof SendResult) {
            SendResult sendResult = (SendResult) result;
            return String.format("发送成功,偏移量:%s, 投递分区:%s, 时间戳:%s",
                    sendResult.getRecordMetadata().offset(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().timestamp());
        }
        return String.format("发送成功,结果不是result:%s", result);//.getRecordMetadata().offset());
    }
}
