package com.example.springutilsdemo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/7/5 14:35
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handlerExp(Exception exp) {
        if (exp instanceof MethodArgumentNotValidException) {
            // 拦截@Valid导致的异常
            var msg = "参数校验错误:" + exp.getClass().getName() + ": " + exp.getMessage();
            return new Result().setCode(500).setMsg(msg);
        }
        var msg = exp.getClass().getName() + ": " + exp.getMessage();
        return new Result().setCode(500).setMsg(msg);
    }

    @Data
    @Accessors(chain = true)
    public class Result {
        private int code;
        private String msg;
        private Object data;
    }
}
