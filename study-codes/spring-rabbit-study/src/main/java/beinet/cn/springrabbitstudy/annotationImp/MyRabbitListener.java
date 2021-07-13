package beinet.cn.springrabbitstudy.annotationImp;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.lang.annotation.Annotation;

/**
 * 仅 bindings() 有变化，重新封装QueueBinding
 */
public class MyRabbitListener implements RabbitListener {
    private RabbitListener rabbitListener;

    public MyRabbitListener(RabbitListener rabbitListener) {
        this.rabbitListener = rabbitListener;
    }

    @Override
    public String id() {
        return rabbitListener.id();
    }

    @Override
    public String containerFactory() {
        return rabbitListener.containerFactory();
    }

    @Override
    public String[] queues() {
        return rabbitListener.queues();
    }

    @Override
    public Queue[] queuesToDeclare() {
        return rabbitListener.queuesToDeclare();
    }

    @Override
    public boolean exclusive() {
        return rabbitListener.exclusive();
    }

    @Override
    public String priority() {
        return rabbitListener.priority();
    }

    @Override
    public String admin() {
        return rabbitListener.admin();
    }

    @Override
    public QueueBinding[] bindings() {
        QueueBinding[] ret = rabbitListener.bindings();
        if (ret != null && ret.length > 0) {
            QueueBinding[] newResult = new QueueBinding[ret.length];
            int idx = 0;
            for (QueueBinding binding : ret) {
                MyQueueBinding newBind = new MyQueueBinding(binding);
                newResult[idx] = newBind;
                idx++;
            }

            ret = newResult;
        }
        return ret;
    }

    @Override
    public String group() {
        return rabbitListener.group();
    }

    @Override
    public String returnExceptions() {
        return rabbitListener.returnExceptions();
    }

    @Override
    public String errorHandler() {
        return rabbitListener.errorHandler();
    }

    @Override
    public String concurrency() {
        return rabbitListener.concurrency();
    }

    @Override
    public String autoStartup() {
        return rabbitListener.autoStartup();
    }

    @Override
    public String executor() {
        return rabbitListener.executor();
    }

    @Override
    public String ackMode() {
        return rabbitListener.ackMode();
    }

    @Override
    public String replyPostProcessor() {
        return rabbitListener.replyPostProcessor();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return rabbitListener.annotationType();
    }
}
