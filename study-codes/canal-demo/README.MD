- 部署canal的服务端  
  - 参考 https://github.com/alibaba/canal/wiki/QuickStart
    - 修改MySQL的my.cnf,开启 Binlog 写入功能：
    
    ```ini
    [mysqld]
    log-bin=mysql-bin # 开启 binlog
    binlog-format=ROW # 选择 ROW 模式
    server_id=1 # 配置 MySQL replaction 需要定义，不要和 canal 的 slaveId 重复
    ```
    
    - 为canal服务端添加slave同步账号：
    ```sql
    CREATE USER canal IDENTIFIED BY 'canal';  
    GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
    ```
    - 记得重启MySQL，让配置生效
    
  - 确保canal所在的服务端，安装了jdk运行环境，可以下载rpm安装：  rpm -i jdk-8u281-linux-x64.rpm
    
  - canal服务端安装：
    - 下载 https://github.com/alibaba/canal/releases 这里是canal.deployer-1.1.5.tar.gz
    - 解压 mkdir canal && tar zxvf canal.deployer-1.1.5.tar.gz -C ./canal/ && cd canal
    - 配置 vim conf/example/instance.properties  要改4个地方：
    ```ini
    canal.instance.mysql.slaveId=103031            # 在主从环境中，保持唯一即可
    canal.instance.master.address=10.2.5.2:3306
    canal.instance.dbUsername=canal
    canal.instance.dbPassword=canal
    ```
    - 启动  sh bin/startup.sh 出现这个表示成功了 `cd to /root/canal for continue`
    - 看服务启动日志  tail logs/canal/canal.log
    - 看数据库实例日志 tail logs/example/example.log
    - 注：默认启动端口为 11111 配置在 conf/canal.properties
    
- canal客户端开发与安装
  - 下载并编译安装starter库： https://github.com/chenqian56131/spring-boot-starter-canal
    - 注：建议升级该starter库的spring依赖版本和官方canal版本
  - 新建spring boot项目，引用这个 spring-boot-starter-canal
  - 在新项目的application.yml里添加canal服务端配置：
    ```yaml
    canal.client.instances.example:
      host: 10.2.30.31
      port: 11111
      batchSize: 1000
    ```
  - 在新项目的启动类上，添加注解： `@EnableCanalClient` 启用canal
  - 新建监听类 `MySQLHandler`, 具体代码参考项目