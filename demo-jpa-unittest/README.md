# JPA单元测试Demo

演示如何在单元测试中，测试与数据库相关的代码，仅限JPA访问数据库
<br><br>
## 依赖
```xml
<!-- JPA操作库 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<!-- 内存数据库，用于测试 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
<!-- 物理数据库，MySql -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```
<br><br>
## 配置
```yaml
# 在yml配置里添加一个使用h2数据库的配置节，指定为test
---
spring:
  profiles: test # 单元测试，通过注解 ActiveProfiles 使用

  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: update  # 实体与数据库不一致时，自动修改表结构
---
```
<br><br>
## 单元测试
直接注入JpaRepository的扩展接口，进行测试即可