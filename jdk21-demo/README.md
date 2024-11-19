# 微服务设计-后端代码仓库

swagger地址：
/swagger-ui/index.html

## 模块说明

``` 
├── beinet-business -- 具体的业务基础模块
│   ├─ beinet-auth  -- token认证服务
│   ├─ beinet-cipher  -- 加密服务，后端内部敏感数据加解密用，不面向前端
│   ├─ beinet-consumer  -- 消息消费模块，消费beinet-log收到的心跳/日志，并进行分发处理
│   ├─ beinet-log-biz  -- 日志持久化服务
│   ├─ beinet-permission  -- 权限服务
│   ├─ beinet-system  -- 系统服务，基础数据维护，如字典表、国际化等
│   ├─ beinet-tenant  -- 租户服务
│   ├─ beinet-user  -- 用户服务
├── beinet-core -- 通用模块
│   ├─ beinet-base  -- 通用dto、常量等定义所在模块
│   ├─ beinet-cache  -- 统一缓存
│   ├─ beinet-config-refresh  -- 配置刷新通用模块
│   ├─ beinet-dao  -- 仓储层基础包
│   ├─ beinet-gateway  -- gateway基础包
│   ├─ beinet-kafka  -- kafka消息队列基础包
│   ├─ beinet-message  -- 发消息基础包，如发邮件，发钉钉通知等
│   ├─ beinet-redis  -- redis基础包
│   ├─ beinet-storage  -- 存储服务，如上传到s3或oss
│   ├─ beinet-utils  -- 工具包
│   ├─ beinet-web  -- web基础包
├── beinet-deployment -- 用于打包部署的模块
│   ├─ beinet-dp-admin  -- 运维后台，不得引用和调用business模块
│   ├─ beinet-dp-app  -- 业务聚合服务，面向客户端/前端
│   ├─ beinet-dp-business  -- 业务基础服务，一般仅面向app层，不得面向客户端/前端
│   ├─ beinet-dp-cipher  -- 加密服务，内部用，禁止面向客户端/前端
│   ├─ beinet-dp-config  -- 配置中心，使用git存储
│   ├─ beinet-dp-gateway  -- 网关，请求认证，请求加密，转发外部代理请求
│   ├─ beinet-dp-job  -- job服务
│   ├─ beinet-dp-third  -- 依赖的第三方服务，比如邮件系统（避免崩溃影响正常业务）
├── beinet-log -- 心跳-日志接收服务（独立服务部署，不在网关背后）
├── beinet-sdk -- 对外提供的sdk模块，主要是dto、常量、错误码以及FeignClient定义(用于RestController实现)，模块间通过feign调用
│   ├─ beinet-auth-sdk  -- 认证服务sdk
│   ├─ beinet-cipher-sdk  -- 加密服务sdk
│   ├─ beinet-log-sdk  -- 基础日志服务sdk，如日志上报
│   ├─ beinet-permission-sdk  -- 权限服务sdk
│   ├─ beinet-system-sdk  -- 系统服务sdk
│   ├─ beinet-tenant-sdk  -- 租户服务sdk
│   ├─ beinet-user-sdk  -- 用户服务sdk
```