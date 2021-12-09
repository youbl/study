package beinet.cn.springkafkaconsumerstudy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2021/12/9 12:47
 */
@Component
public class KafkaConsumer {

    // KafkaListener如果设置了属性id，则id将作为groupId使用（会覆盖yml里的group-id），给kafka保存offset；可以设置属性idIsGroup=false
    @KafkaListener(id = "handler1", idIsGroup = false,
            //topics = {"${spring.kafka.template.default-topic}"},  // topics写入一个个的topic
            topicPattern = "task_execute_result.*",                 // topicPattern使用正则匹配topic名称
            containerFactory = "myKafkaFactory")
    // 注意，必须设置factory的AckMode为AckMode.MANUAL_IMMEDIATE 才能会出现这个Acknowledgment参数
    public void msgHandler(List<ConsumerRecord> message, Acknowledgment ack) {
        int errIndex = 0;
        try {
            for (ConsumerRecord record : message) {
                System.out.println(record);
//                if (errIndex > 5)
//                    throw new Exception("aabbcc");
                errIndex++;
            }
            ack.acknowledge(); // 提交偏移量
        } catch (Exception exp) {
            // nack, 参数1为出错的索引，这样已处理的数据会提交偏移量，未提交的会重新处理
            // 注意：消费消息要考虑幂等
            ack.nack(errIndex, 1);
        }
    }
    //
}
