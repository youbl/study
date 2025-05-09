package beinet.cn.springkafkaconsumerstudy;

import lombok.Data;
import lombok.ToString;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 新类
 * @author youbl
 * @since 2025/5/9 13:52
 */
@Component
public class TestConsumer {

    // 注意：
    // 这个消费者方法会接收批量的消息，message格式如下： {"aa":1},{"aa":2}
    // JSONUtil.toBean不会报错，只会把第一条消息进行序列化，后续{"aa":2}数据就抛弃了，并且是批量进行ack了
    // 注意
    @KafkaListener(topics = "aaa", containerFactory = "myBatchKafkaFactory", groupId = "hhh")
    public void stopRuntime(String message, Acknowledgment acknowledgment) {
        System.out.println(message);
        UserLogsDto item = cn.hutool.json.JSONUtil.toBean(message, UserLogsDto.class);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "aaa", containerFactory = "singleKafkaFactory", groupId = "ggg")
    public void consumeSingleMsg(String message, Acknowledgment acknowledgment) {
        System.out.println(message);
        UserLogsDto item = cn.hutool.json.JSONUtil.toBean(message, UserLogsDto.class);
        acknowledgment.acknowledge();
    }


    @Data
    @ToString
    public class UserLogsDto {
        private Long id;
        private String reportType;
        private String subType;
        private Long reportTime;
        private String deviceNo;
        private Long userId;
        private Long teamId;
        private Long memberId;
    }
}
