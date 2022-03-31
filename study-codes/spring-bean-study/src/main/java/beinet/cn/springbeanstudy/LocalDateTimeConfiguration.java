package beinet.cn.springbeanstudy;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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
    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }
}
