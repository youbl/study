package cn.beinet.sdk.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 事件日志对象
 */
@Data
@Accessors(chain = true)
@Slf4j
@Schema(description = "事件日志对象")
public class EventDto {

    @Schema(description = "事件日志id")
    private Long id;

    @Size(max = 50)
    @Schema(description = "事件主类型")
    private String mainType;

    @Size(max = 50)
    @Schema(description = "事件子类型")
    private String subType;

    @Schema(description = "事件上报毫秒级时间戳")
    private Long reportTime;

    @Schema(description = "事件归属用户id")
    private Long userId;

    @Schema(description = "事件归属租户id")
    private Long tenantId;

    @Schema(description = "事件链路id")
    private String traceId;

    @Schema(description = "事件数据")
    private Map<String, Object> extInfo;
}
