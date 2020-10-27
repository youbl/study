# Redis单元测试Demo

一个演示如何在单元测试中，测试与Redis相关的代码
<br><br>
## 依赖
```xml
<!-- spring 的redis操作库 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- 一个嵌入的redis服务进程，scope设定它只作用于test测试域，不会发布 -->
<dependency>
    <groupId>it.ozimov</groupId>
    <artifactId>embedded-redis</artifactId>
    <version>0.7.3</version>
    <scope>test</scope>
</dependency>
```
<br><br>
## 配置
```yaml
# 在yml配置里添加一个配置节，指定为test
---
spring:
  profiles: test # 单元测试，通过注解 ActiveProfiles 使用这一段，默认调用localhost:6379的Redis
---
```
<br><br>
## 单元测试
通过`@PostConstruct`注解，启动嵌入的Redis，然后再运行Redis依赖代码
```java
@SpringBootTest
@ActiveProfiles("test") // 使用yml里的test配置
class DemoRedisUnittestApplicationTests {

    private RedisServer redisServer;
    @PostConstruct
    public void startRedis() {
        // https://github.com/kstyrc/embedded-redis/issues/51
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 128M") //maxheap 128M
                .build();

        redisServer.start();
    }
```
