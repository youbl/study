package beinet.cn.springjpastudy.beans;

import java.lang.annotation.*;

/**
 * 使用此注解来切换数据源,value表示要使用的数据源
 */
@Target({ElementType.METHOD, ElementType.TYPE})// , ElementType.PARAMETER
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MySource {
    String value(); // 要使用的数据源，必填
}
