package cn.beinet.sdk.event.annotation;

import cn.beinet.sdk.event.enums.EventSubType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加在方法上，表示该方法需要上报事件日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventLog {

    /**
     * 事件子类型
     */
    EventSubType subType();

    /**
     * 方法返回值为ResponseData时，ResponseData.code等于什么值时要上报日志。默认值0-仅成功时记录。设置为all-无论成败都要上报
     */
    String logCode() default "0";
}
