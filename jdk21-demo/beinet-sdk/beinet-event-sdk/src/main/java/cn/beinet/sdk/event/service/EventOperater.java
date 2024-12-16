package cn.beinet.sdk.event.service;

import cn.beinet.sdk.event.dto.EventDto;
import cn.beinet.sdk.event.enums.EventMainType;
import cn.beinet.sdk.event.enums.EventSubType;

/**
 * 事件日志处理器接口类。
 * 后续根据实际情况，实现自己的接口来处理日志
 *
 * @author youbl
 * @since 2024/12/13 15:38
 */
public interface EventOperater {
    /**
     * 处理器支持的事件子类型
     * @return 子类型
     */
    EventSubType handleType();

    /**
     * 处理器支持的事件父类型
     * @return 父类型
     */
    default EventMainType handleMainType() {
        EventSubType subType = handleType();
        if (subType == null) {
            return null;
        }
        return subType.getMainType();
    }

    /**
     * 事件处理
     * @param event 事件对象
     */
    void operate(EventDto event);
}
