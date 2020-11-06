package beinet.cn.demologcustomize;

import java.lang.annotation.*;

/**
 * 用了这个注解的方法，都应该记录info日志
 */
@Target(ElementType.METHOD) // 表示NeedLog注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 修饰注解的生命周期，RUNTIME表示jvm加载class文件后依旧存在
@Documented
public @interface NeedLog {
    /**
     * 备注，此文字也会记录到日志里
     *
     * @return 备注
     */
    String value() default "";

    /**
     * 是否记录方法返回值，默认true
     *
     * @return 是否
     */
    boolean logReturn() default true;

    /**
     * 出现异常时是否记录为error级别，默认false
     *
     * @return 是否
     */
    boolean logExceptionAsError() default false;
}
