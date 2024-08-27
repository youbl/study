package beinet.cn.awss3demo.demos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
//@ApiModel(description = "服务统一响应实体")
public class ResponseData<T> {
    //@ApiModelProperty(value = "响应码，0表示成功，>0表示失败")
    private int code;

    //@ApiModelProperty(value = "响应消息，code>0时返回错误信息")
    private String msg;

    //@ApiModelProperty(value = "响应数据")
    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time = LocalDateTime.now();

    private String status;
    private String stackTrace;
    private Boolean ignorePrintStack = false;

    public static <T> ResponseData<T> ok(T data) {
        ResponseData<T> res = new ResponseData<>();
        res.setData(data);
        res.setStatus("0");
        return res;
    }

    public static <T> ResponseData<T> ok() {
        return ok(null);
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

    @JsonIgnore
    public Boolean isSuccess() {
        return this.code == 0;
    }
}
