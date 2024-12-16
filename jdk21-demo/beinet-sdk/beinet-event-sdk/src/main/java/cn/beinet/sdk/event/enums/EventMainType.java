package cn.beinet.sdk.event.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 上报事件主类型
 */
@Getter
public enum EventMainType {
    @Schema(description = "运维后台操作")
    SYS_ADMIN,

    @Schema(description = "登录行为")
    LOGIN,

    @Schema(description = "用户行为")
    USER,

    @Schema(description = "授权行为")
    AUTH,

    @Schema(description = "文件管理行为")
    FILE,
    ;

    public boolean match(String module) {
        return this.toString().equalsIgnoreCase(module);
    }
}
