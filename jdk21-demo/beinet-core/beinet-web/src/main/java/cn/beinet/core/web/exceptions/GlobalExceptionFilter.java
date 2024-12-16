package cn.beinet.core.web.exceptions;

import cn.beinet.core.base.commonDto.ResponseData;
import cn.beinet.core.base.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2021/11/23 14:41
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionFilter {
    @ExceptionHandler(value = Exception.class)
    public ResponseData exceptionHandler(Exception e) {
        //System.out.println("未知异常！原因是:" + e);
        log.error("全局异常: {}", e.getMessage());

        int errCode = getErrorCode(e);
        String msg = getMessage(e);
        return ResponseData.fail(errCode, msg);
    }

    private int getErrorCode(Exception e) {
        if (e instanceof BaseException) {
            return ((BaseException) e).getCode();
        }
        return 500;
    }

    private String getMessage(Exception e) {
        String ret = "";
        if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            ret = be.getMessage();
        }
        if (!StringUtils.hasLength(ret)) {
            if (e.getCause() != null) {
                ret = e.getCause().getMessage();
            } else {
                ret = e.getMessage();
            }
        }
        return ret;
    }
}
