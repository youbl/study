package cn.beinet.sdk.event.service.implement;

import cn.beinet.core.base.configs.SystemConst;
import cn.beinet.core.utils.FileHelper;
import cn.beinet.sdk.event.dto.EventDto;
import cn.beinet.sdk.event.enums.EventSubType;
import cn.beinet.sdk.event.service.EventOperater;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 上传日志写入文件
 * @author youbl
 * @since 2024/12/13 16:01
 */
@Component
public class EventFileSaver implements EventOperater {
    private final static String FILE = SystemConst.getBaseDir() + "upload.log";

    private ObjectMapper objectMapper;

    @Override
    public EventSubType handleType() {
        return EventSubType.FILE_UPLOAD;
    }

    @Override
    @SneakyThrows
    public void operate(EventDto event) {
        String json = getObjectMapper().writeValueAsString(event) + System.lineSeparator();
        FileHelper.saveFile(FILE, json, false);
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            // 设置属性为 null 时不序列化
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return objectMapper;
    }
}
