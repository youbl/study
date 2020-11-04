# MongoDB单元测试Demo

一个演示如何在单元测试中，测试与 MongoDB 相关的代码
<br><br>
## 依赖
```xml
<!-- spring 的mongo操作库 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<!-- 测试时会下载并启动mongo服务进程，scope设定它只作用于test测试域，不会发布 -->
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <version>2.2.0</version>
    <scope>test</scope>
</dependency>
```
<br><br>
## 配置
```yaml
# 在yml配置里添加一个配置节，指定为test
---
spring:
  profiles: test # 单元测试，通过注解 ActiveProfiles 使用这一段
---
```
<br><br>
## 单元测试
通过`@PostConstruct`注解，下载并启动MongoDB，然后再运行依赖代码
```java
@SpringBootTest
@ActiveProfiles("unittest")
class DemoMongoUnittestApplicationTests {
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private MongodExecutable mongodExecutable;
    private MongodProcess mongod;

    /**
     * 启动mock的mongo
     * 注意：首次启动测试，会在宿主机下载相应版本的Mongo，所以会比较慢。
     *
     * @throws Exception 可能的异常
     */
    @PostConstruct
    void startMongo() throws Exception {
        // 超过3.4的以上版本，会报错： Could not start process: <EOF>
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V3_4)
                .net(new Net(12345, Network.localhostIsIPv6())).build());
        mongod = mongodExecutable.start();
    }

    /**
     * 停止mock的mongo
     */
    @PreDestroy
    void stopMongo() {
        mongod.stop();
        mongodExecutable.stop();
    }
```
