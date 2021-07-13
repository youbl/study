package beinet.cn.springrabbitstudy.annotationImp;

import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;

import java.lang.annotation.Annotation;

/**
 * 仅 value() 有变化，重新封装 Queue
 */
public class MyQueueBinding implements QueueBinding {
    private QueueBinding queueBinding;

    public MyQueueBinding(QueueBinding queueBinding) {
        this.queueBinding = queueBinding;
    }

    @Override
    public Queue value() {
        Queue queue = queueBinding.value();
        if (queue == null) {
            return null;
        }
        return new MyQueue(queue);
    }

    @Override
    public Exchange exchange() {
        return queueBinding.exchange();
    }

    @Override
    public String[] key() {
        return queueBinding.key();
    }

    @Override
    public String ignoreDeclarationExceptions() {
        return queueBinding.ignoreDeclarationExceptions();
    }

    @Override
    public Argument[] arguments() {
        return queueBinding.arguments();
    }

    @Override
    public String declare() {
        return queueBinding.declare();
    }

    @Override
    public String[] admins() {
        return queueBinding.admins();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return queueBinding.annotationType();
    }
}
