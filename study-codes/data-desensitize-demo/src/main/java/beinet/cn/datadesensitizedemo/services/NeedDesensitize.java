package beinet.cn.datadesensitizedemo.services;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 注解，加了这个注解的属性需要进行脱敏处理
 *
 * @author youbl
 * @since 2023/6/29 20:06
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizeSerializer.class)
public @interface NeedDesensitize {

    /**
     * 当前字段要按什么类型进行脱敏
     *
     * @return 枚举
     */
    //@AliasFor("method")
    DesensitizeEnum value();
}
