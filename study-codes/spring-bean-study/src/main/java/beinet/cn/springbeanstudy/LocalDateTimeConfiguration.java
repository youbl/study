package beinet.cn.springbeanstudy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/31 9:58
 */
@Configuration
public class LocalDateTimeConfiguration {
    // 支持多种格式反序列化
    // yyyy-MM-dd HH:mm:ss
    // yyyy-MM-dd'T'HH:mm:ss
    // yyyy-MM-dd'T'HH:mm:ss.SSS
    // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    protected static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral(' ')        // ' ' made optional
            .optionalEnd()
            .optionalStart()
            .appendLiteral('T')        // 'T' made optional
            .optionalEnd()
            .optionalStart()
            .append(ISO_LOCAL_TIME)    // time made optional
            .optionalEnd()
            .optionalStart()           // zone and offset made optional
            .appendOffsetId()
            .optionalEnd()
            .optionalStart()
            //为了兼容时区不带:这种格式
            .appendOffset("+HHMM", "Z")
            .optionalEnd()
            .optionalStart()
            .appendLiteral('[')
            .parseCaseSensitive()
            .appendZoneRegionId()
            .appendLiteral(']')
            .optionalEnd()
            .toFormatter();
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    // 方案一
    // 指定反序列化支持的日期格式，如前端传递字符串给Controller
    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(dateTimeFormatter);
    }

    // 指定序列化支持的日期格式，如Controller返回日期
    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return builder -> builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer())
//                .serializerByType(LocalDateTime.class, localDateTimeSerializer());
//    }

    // 方案二
    @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer());
        // 不能添加2次同一个类型的反序列化器
        // javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);  // 针对Long类型
        javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);   // 针对long简单类型

        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }
}
