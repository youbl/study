package beinet.cn.springrabbitstudy.annotationImp;

import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Queue;

import java.lang.annotation.Annotation;

/**
 * name按需要进行修改
 */
public class MyQueue implements Queue {
    private Queue queue;
    private String grayName;

    public MyQueue(Queue queue) {
        this.queue = queue;
        this.grayName = this.queue.name() + "_IamGray";
    }

    @Override
    public String value() {
        return grayName;
    }

    @Override
    public String name() {
        return grayName;
    }

    @Override
    public String durable() {
        return queue.durable();
    }

    @Override
    public String exclusive() {
        return queue.exclusive();
    }

    @Override
    public String autoDelete() {
        return queue.autoDelete();
    }

    @Override
    public String ignoreDeclarationExceptions() {
        return queue.ignoreDeclarationExceptions();
    }

    @Override
    public Argument[] arguments() {
        return queue.arguments();
    }

    @Override
    public String declare() {
        return queue.declare();
    }

    @Override
    public String[] admins() {
        return queue.admins();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return queue.annotationType();
    }
}
