package beinet.cn.frontstudy.longLoopTest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

/**
 * 新类
 *
 * @author youbl
 * @since 2024/6/1 11:11
 */
@ControllerAdvice
public class LongLoopRequestTimeoutHandler {
    // 长轮询请求超时，会抛出 AsyncRequestTimeoutException 异常，这边返回304状态即可
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    @ResponseBody
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public String asyncRequestTimeoutHandler(AsyncRequestTimeoutException e) {
        System.out.println("异步请求超时");
        return "304";
    }
}
