package beinet.cn.datadesensitizedemo.services;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 脱敏用的序列化类
 * 注：此类实现了2个功能
 * 1、序列化器实现，用于实现脱敏处理
 * 2、为指定的属性查找序列化器，如果这个属性定义了脱敏注解，则返回自己，否则查找其它合适的序列化器
 *
 * @author youbl
 * @since 2023/6/29 20:07
 */
public class DesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizeEnum desensitizeEnum;

    /**
     * 对指定的字符串s，进行脱敏后再序列化
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 由 com.fasterxml.jackson.databind.ser.BeanPropertyWriter类的serializeAsField方法调用，字符串s不可能为null，所以无须判断
        jsonGenerator.writeString(desensitizeEnum.getDesensitizeMethod().apply(s));
    }

    /**
     * ContextualSerializer接口定义的方法
     * 查找加了脱敏注解的字段，获取注解的方法暂存.
     * 注：每个属性只会调用一次，然后被缓存
     *
     * @param prov     Serializer provider to use for accessing config, other serializers
     * @param property Method or field that represents the property
     *                 (and is used to access value to serialize).
     *                 Should be available; but there may be cases where caller cannot provide it and
     *                 null is passed instead (in which case impls usually pass 'this' serializer as is)
     * @return JsonSerializer实例，用于序列化某些类的属性
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        NeedDesensitize annotation = property.getAnnotation(NeedDesensitize.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.desensitizeEnum = annotation.value();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
