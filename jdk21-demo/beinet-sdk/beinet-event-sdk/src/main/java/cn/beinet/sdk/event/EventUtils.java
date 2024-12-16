package cn.beinet.sdk.event;

import cn.beinet.core.utils.SpringHelper;
import cn.beinet.sdk.event.enums.EventSubType;
import cn.beinet.sdk.event.service.EventFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 事件上传辅助类
 * @author youbl
 * @since 2024/12/13 14:40
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EventUtils {

    private final EventFactory factory;

    /**
     * 事件上报
     * @param subType 事件子类型
     * @param data 事件数据
     */
    public static void report(EventSubType subType, Object data) {
        // 使用Spring的bean才能实现Async效果
        EventUtils instance = SpringHelper.getBean(EventUtils.class);
        instance.reportEvent(subType, data);
    }

    /**
     * 事件上报
     * @param subType 事件子类型
     * @param data 事件数据
     */
    @Async
    public void reportEvent(EventSubType subType, Object data) {
        try {
            factory.reportEvent(subType, data);
            log.info("{}事件已上报: {}", subType, data);
        } catch (Exception e) {
            log.error("事件上传出错:{} {}", subType, data, e);
        }
    }
}
