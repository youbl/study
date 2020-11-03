# RabbitMQ单元测试Demo

演示如何在单元测试中，测试与RabbitMQ消息队列相关的代码
<br><br>
## 依赖
```xml
<!-- RabbitMQ操作库 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<!-- RabbitMQ拦截库，用于测试 -->
<dependency>
    <groupId>com.github.fridujo</groupId>
    <artifactId>rabbitmq-mock</artifactId>
    <version>1.1.1</version>
    <scope>test</scope>
</dependency>
```
<br><br>
## 配置
在单元测试项目下新建配置类，替换掉默认的ConnectionFactory
```java
package beinet.cn.demorabbitmqunittest;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockRabbitConfig {
    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new MockConnectionFactory());
    }
}

```
<br><br>
## 单元测试
直接进行测试即可