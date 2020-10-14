package com.example.dblogdemo.db;

import com.p6spy.engine.spy.option.SystemProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

// 配置详细说明参考： https://p6spy.readthedocs.io/en/latest/configandusage.html
// 开发参考： https://github.com/klboke/p6spy-spring-boot-starter
/*
application.yml里的配置参考，不加配置时，以下是默认值：
p6spy:
  config:
    driverlist: "com.mysql.cj.jdbc.Driver"
    autoflush: "true"
    dateformat: "yyyy-MM-dd- HH:mm:ss:SSS"
    appender: "com.p6spy.engine.spy.appender.Slf4JLogger"
    logfile: "spy.log"
    logMessageFormat: "com.p6spy.engine.spy.appender.CustomLineFormat"
    customLogMessageFormat: "连接:%(connectionId)|时长:%(executionTime)|sql:[%(category)]%(sqlSingleLine)"
 */
@Data
@ConfigurationProperties(prefix = "p6spy.config")// SystemProperties.P6SPY_PREFIX
public class P6SpyProperties {

    private String driverlist = "com.mysql.cj.jdbc.Driver";

    private String autoflush = "true";

    private String dateformat = "yyyy-MM-dd- HH:mm:ss:SSS";

    private String appender = "com.p6spy.engine.spy.appender.Slf4JLogger";

    private String logfile = "spy.log";

    private String logMessageFormat = "com.p6spy.engine.spy.appender.CustomLineFormat";
    //private String logMessageFormat = "com.p6spy.engine.spy.appender.MultiLineFormat";

    private String customLogMessageFormat = "连接:%(connectionId)|时长:%(executionTime)|sql:[%(category)]%(sqlSingleLine)";

    private String excludecategories = "info,debug,result,resultset,batch";
}
