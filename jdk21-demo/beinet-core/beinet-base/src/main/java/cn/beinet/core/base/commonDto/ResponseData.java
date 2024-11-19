package cn.beinet.core.base.commonDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Controller统一响应实体
 *
 * @author youbl
 * @since 2024/7/18 16:13
 */
@Data
@Schema(description = "统一响应实体")
public class ResponseData<T> {
    @Schema(description = "响应码，0表示成功，>0表示失败")
    private int code;

    @Schema(description = "响应消息，code>0时返回的错误参考信息")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "服务端时间戳")
    private long ts = System.currentTimeMillis();

    //@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    //private LocalDateTime time = LocalDateTime.now();

    @JsonIgnore
    public Boolean isSuccess() {
        return this.code == 0;
    }


    public static <T> ResponseData<T> ok() {
        return new ResponseData<>();
    }

    public static <T> ResponseData<T> ok(T data) {
        ResponseData<T> res = new ResponseData<>();
        res.setData(data);
        return res;
    }

    public static <T> ResponseData<T> fail(int errorCode, String errorMsg) {
        return fail(errorCode, errorMsg, null);
    }

    public static <T> ResponseData<T> fail(int errorCode, String errorMsg, T data) {
        ResponseData<T> res = new ResponseData<>();
        res.setCode(errorCode);
        res.setMsg(errorMsg);
        res.setData(data);
        return res;
    }
}
