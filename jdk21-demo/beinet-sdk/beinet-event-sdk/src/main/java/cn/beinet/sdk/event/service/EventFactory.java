package cn.beinet.sdk.event.service;

import cn.beinet.core.base.configs.SystemConst;
import cn.beinet.core.web.context.ContextUtils;
import cn.beinet.sdk.event.dto.EventDto;
import cn.beinet.sdk.event.enums.EventSubType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author youbl
 * @since 2024/12/13 15:34
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventFactory {
    private final List<EventOperater> operaters;

    public void reportEvent(EventSubType subType, Object data) {
        Assert.notNull(subType, "subType must not be null");

        EventDto eventDto = combineData(subType, data);
        for (EventOperater operater : operaters) {
            // 配置了子类型，或只配置了主类型时
            if (subType.equals(operater.handleType()) ||
                    (operater.handleType() == null && subType.getMainType().equals(operater.handleMainType()))) {
                operater.operate(eventDto);
            }
        }
    }

    private EventDto combineData(EventSubType subType, Object data) {
        Map<String, Object> reportData = getContextVar();
        mergeData(reportData, data);
        return new EventDto()
                .setSubType(subType.name())
                .setMainType(subType.getMainType().name())
                .setReportTime(System.currentTimeMillis())
                .setExtInfo(reportData);
    }

    // 收集上下文里的数据，用于日志上报
    private Map<String, Object> getContextVar() {
        var reportData = new HashMap<String, Object>();
        var requestTime = ContextUtils.getRequestTime();
        long cost = 0;
        if (requestTime > 0) {
            cost = System.currentTimeMillis() - requestTime;// 处理耗时
        }
        String clientIp = "";
        String serverIp = "";
        String url = "";
        try {
            clientIp = ContextUtils.getFullIp();
            url = ContextUtils.getRequestUrl(true, true);
            serverIp = SystemConst.getServerIp();
        } catch (Exception exp) {
            log.warn("获取上下文出错:", exp);
        }

        reportData.put("ip", clientIp);
        reportData.put("sip", serverIp);
        if (requestTime > 0) {
            reportData.put("ts", String.valueOf(requestTime));
            reportData.put("cost", String.valueOf(cost) + "ms");
        }
        reportData.put("url", url);
        return reportData;
    }

    // 把data合并进reportData， 不改变data
    private void mergeData(Map<String, Object> reportData, Object data) {
        if (data instanceof Map<?, ?> dataMap) {
            // 使用安全的方式将data转换为Map
            for (Map.Entry<?, ?> entry : dataMap.entrySet()) {
                if (entry.getKey() instanceof String) {
                    reportData.put((String) entry.getKey(), entry.getValue());
                }
            }
        } else {
            if (data == null) {
                data = "";
            }
            reportData.put("data", data);
        }
    }
}
