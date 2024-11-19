package cn.beinet.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Slf4j
public class JsonHelper {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+0"));
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //日期序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        //日期反序列化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("对象转换json失败,errMsg:{}", e.getMessage(), e);
        }
        return null;
    }


    public static <T> T toBean(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, clz);
        } catch (Exception e) {
            log.error("json转换对象失败,errMsg:{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toBean(String json, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            log.warn("json转换对象失败,errMsg:{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            log.error("json转换对象失败,errMsg:{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            return objectMapper.convertValue(fromValue, toValueTypeRef);
        } catch (Exception e) {
            log.error("json转换对象失败,errMsg:{}", e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> toListBean(String json, Class<T> clz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clz);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.warn("json转换对象失败, errMsg:{}", e.getMessage(), e);
        }
        return null;
    }

}
