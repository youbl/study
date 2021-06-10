# spring-cloud体验项目

- 启动顺序：
    1. 启动2个Eureka注册中心，分别监听9001和9002端口：
        1. nohup java -Dserver.port=9001 -Deureka.instance.appname=eureka1 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -Deureka.client.service-url.defaultZone=http://localhost:9001/eureka,http://localhost:9002/eureka -jar /data/apps/eureka/demo-eureka1-0.0.1-SNAPSHOT.jar 2>&1 &
        2. nohup java -Dserver.port=9002 -Deureka.instance.appname=eureka2 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -Deureka.client.service-url.defaultZone=http://localhost:9001/eureka,http://localhost:9002/eureka -jar /data/apps/eureka/demo-eureka1-0.0.1-SNAPSHOT.jar 2>&1 &
    2. 启动2个demo-mall服务，分别监听9003和9004端口：
        1. nohup java -Dserver.port=9003 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -jar demo-mall-0.0.1-SNAPSHOT.jar 2>&1 &
        2. nohup java -Dserver.port=9004 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -jar demo-mall-0.0.1-SNAPSHOT.jar 2>&1 &
    3. 启动2个demo-product服务，分别监听9005和9006端口：
        1. nohup java -Dserver.port=9005 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -jar demo-product-0.0.1-SNAPSHOT.jar 2>&1 &
        2. nohup java -Dserver.port=9006 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -jar demo-product-0.0.1-SNAPSHOT.jar 2>&1 &
    4. 启动网关，监听9000端口：
        1. nohup java -Dserver.port=9000 -Xms1024m -Xmx1024m -Xmn384m -XX:+UseConcMarkSweepGC -jar demo-gateway-server-0.0.1-SNAPSHOT.jar 2>&1 &

- 好了，可以访问了：
    * 查看注册上来的服务状态： http://127.0.0.1:9001/ 或 http://127.0.0.1:9002/
    * 访问mall服务： http://127.0.0.1:9000/demo-mall/
    * 访问product服务： http://127.0.0.1:9000/demo-product/

    