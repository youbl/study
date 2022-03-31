package beinet.cn.springbeanstudy;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/31 9:58
 */
@Configuration
public class LocalDateTimeConfiguration {
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    // 方案一
    // 指定反序列化支持的日期格式，如前端传递字符串给Controller
    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern));
    }

    // 指定序列化支持的日期格式，如Controller返回日期
    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer())
                .serializerByType(LocalDateTime.class, localDateTimeSerializer());
    }
}
