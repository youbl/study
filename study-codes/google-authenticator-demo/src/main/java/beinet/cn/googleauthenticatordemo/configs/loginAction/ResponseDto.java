package beinet.cn.googleauthenticatordemo.configs.loginAction;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 9:53
 */
@Data
@Accessors(chain = true)
public class ResponseDto {
    private int code;
    private String msg;
    private Object data;

    public static ResponseDto ok() {
        return new ResponseDto()
                .setCode(200)
                .setMsg("OK");
    }

    public static ResponseDto ok(Object data) {
        return new ResponseDto()
                .setCode(200)
                .setMsg("OK")
                .setData(data);
    }

    public static ResponseDto fail(String msg) {
        return new ResponseDto()
                .setCode(500)
                .setMsg(msg);
    }
}
