package cn.beinet.core.base.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
    private int code;
    private String msg;
    private Object data;

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public BaseException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(BaseErrorEnums error) {
        this(error.getErrorCode(), null, null);
    }

    public BaseException(BaseErrorEnums error, String msg) {
        this(error.getErrorCode(), msg, null);
    }

    public BaseException(BaseErrorEnums error, Throwable cause) {
        this(error.getErrorCode(), error.getErrorMsg(), cause);
    }

    public static BaseException of(BaseErrorEnums error) {
        return new BaseException(error);
    }

    public static BaseException of(BaseErrorEnums error, Throwable cause) {
        return new BaseException(error, cause);
    }

    public static BaseException of(BaseErrorEnums error, String msg) {
        return new BaseException(error, msg);
    }

    public static BaseException of(BaseErrorEnums error, Object data) {
        BaseException baseException = new BaseException(error);
        baseException.setData(data);
        return baseException;
    }
}
