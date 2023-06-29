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
 *
 * @author youbl
 * @since 2023/6/29 20:07
 */
public class DesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizeEnum desensitizeEnum;

    /**
     * 脱敏后再序列化
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(desensitizeEnum.getDesensitizeMethod().apply(s));
    }

    /**
     * 查找加了脱敏注解的字段，获取注解的方法暂存
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        NeedDesensitize annotation = beanProperty.getAnnotation(NeedDesensitize.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            this.desensitizeEnum = annotation.value();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
