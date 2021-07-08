package beinet.cn.springrabbitstudy;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Collection;

public class MyRabbitAdmin extends RabbitAdmin {
    private ApplicationContext applicationContext;

    public MyRabbitAdmin(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public MyRabbitAdmin(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void initialize() {
        Collection<Queue> contextQueues =
                this.applicationContext.getBeansOfType(Queue.class).values();
        for (Queue queue : contextQueues) {
            changeName(queue, queue.getName() + "_gray");
            queue.setActualName(queue.getName() + "_gray");
        }
        //declareQueue();

        super.initialize();
    }

    private void changeName(Queue queue, String newName) {
        try {
            Field field = queue.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(queue, newName);
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }
}
