package beinet.cn.xxljobdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XxljobDemoApplication {

    /**
     * 首先要部署xxljob调度服务，参考官方文档：
     * https://github.com/xuxueli/xxl-job
     *
     * 1、下载release版本源码：
     * https://github.com/xuxueli/xxl-job/archive/refs/tags/2.3.0.zip
     *
     * 2、解压zip文件，使用里面的sql创建数据库和相关表：
     * /xxl-job/doc/db/tables_xxl_job.sql
     * 注：可以修改数据库名
     *
     * 3、打开项目，比如 使用IDEA打开项目下的pom.xml
     * 注：xxl-job-admin项目就是调度服务，编译它就可以了
     *
     * 4、修改xxl-job-admin项目的application.properties文件，
     * 4.1、修改数据库配置：
     *      spring.datasource.url
     *      spring.datasource.username
     *      spring.datasource.password
     * 4.2、需要接收告警时，配置一下报警邮箱：
     *      spring.mail.xxx 的相关配置
     * 4.3、配置调度服务与客户端的通信密钥
     *      xxl.job.accessToken=
     *      注：该字段必须双方同时配置，或同时为空
     *
     * 5、点IDEA右侧maven下的package，进行打包
     * 然后用打包好的jar进行部署，比如启动命令：
     * /usr/java/jdk-11/bin/java -jar /data/app/jars/xxl-job-admin-2.3.0.jar
     *
     * 6、OK，项目默认在8080端口，可以开始访问了
     * 注：如果使用nginx转发，参考配置：
     *   location /xxl/ {
     *     proxy_pass http://localhost:8080/; # 8080后有斜杠，表示转发url时，删除xxl这一截字符串后转发
     *     proxy_set_header X-Real-IP $remote_addr;
     *   }
     *   location /xxl-job-admin/ {
     *     proxy_pass http://localhost:8080;
     *     proxy_set_header X-Real-IP $remote_addr;
     *   }
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(XxljobDemoApplication.class, args);
    }

}
