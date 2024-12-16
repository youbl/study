package cn.beinet.core.base.exceptions;

/**
 * 全局错误码的枚举接口类
 * @author youbl
 * @since 2024/12/2 20:43
 */
public interface BaseErrorEnums {
    /**
     * 错误码
     */
    int getErrorCode();

    /**
     * 错误消息
     */
    String getErrorMsg();
}
