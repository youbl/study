package cn.beinet.sdk.event.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 上报事件子类型
 */
@Getter
public enum EventSubType {
    /**
     * 登录相关
     */
    @Schema(description = "登录成功")
    LOGIN_SUCCESS(EventMainType.LOGIN),
    @Schema(description = "登录失败")
    LOGIN_FAIL(EventMainType.LOGIN),

    /**
     * 授权相关
     */
    @Schema(description = "清除token")
    AUTH_TOKEN_CLEAN(EventMainType.AUTH),

    /**
     * 文件操作相关
     */
    @Schema(description = "文件上传")
    FILE_UPLOAD(EventMainType.FILE),
    ;


    private final EventMainType mainType;

    EventSubType(EventMainType mainType) {
        this.mainType = mainType;
    }

    public boolean match(String module) {
        return this.name().equalsIgnoreCase(module);
    }
}
