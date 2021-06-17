package beinet.cn.springbeanstudy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionFilter {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    // 不加ResponseBody会抛异常： Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Circular view path
    public Result handleExp(Exception exp) {
        log.error("所有异常:", exp);
        return Result.error(500, "handleExp:" + exp.getMessage());
    }

    // 注：class NullPointerException extends RuntimeException，所以空指针会走这里
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result handleRuntimeExp(Exception exp) {
        log.error("Runtime异常:", exp);
        return Result.error(500, "handleRuntimeExp:" + exp.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result handleIllegalExp(Exception exp) {
        log.error("Illegal异常:", exp);
        return Result.error(500, "handleIllegalExp:" + exp.getMessage());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        public static Result error(int code, String msg) {
            return new Result(code, msg);
        }

        public static Result success(String msg) {
            return new Result(200, msg);
        }

        private int code;
        private String msg;
    }
}
