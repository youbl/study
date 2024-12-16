package cn.beinet.sdk.event.aspect;

import cn.beinet.core.base.commonDto.ResponseData;
import cn.beinet.core.web.context.ContextUtils;
import cn.beinet.sdk.event.EventUtils;
import cn.beinet.sdk.event.annotation.EventLog;
import cn.beinet.sdk.event.constants.EventConst;
import cn.beinet.sdk.event.enums.EventSubType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author zixun
 */
@Aspect
@Component
@Slf4j
public class EventLogAspect {

    // 配置织入点
    @Pointcut("@annotation(cn.beinet.sdk.event.annotation.EventLog)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 织入点
     * @param result    方法返回结果
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 织入点
     * @param exp       异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "exp")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exp) {
        handleLog(joinPoint, exp, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception exp, Object result) {
        try {
            // 获得注解
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            EventLog opLog = methodSignature.getMethod().getAnnotation(EventLog.class);
            if (opLog == null) {
                return;
            }
            // 判断是否符合记录日志的条件
            if (!validLogCode(opLog.logCode(), result)) {
                return;
            }
            HttpServletRequest request = ContextUtils.getRequest();
            if (request == null) {
                return;
            }

            //String method = request.getMethod();
            //请求参数
            Object params;
            //if ("PUT".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method)) {
            params = filterParam(joinPoint.getArgs());
            //} else {
            // 读取不到url参数，不过也没必要
            //    Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            //    params = paramsMap.toString();
            //}
            EventSubType subType = opLog.subType();
            // 操作日志
            EventUtils.report(subType, params);
        } catch (Exception expInner) {
            log.error("logReportError:", expInner);
        }
    }

    private boolean validLogCode(String logCode, Object result) {
        if (StringUtils.isEmpty(logCode)) {
            return false;
        }
        if (logCode.equals(EventConst.ALL)) {
            return true;
        }
        ResponseData responseData = null;
        if (result instanceof ResponseData) {
            responseData = (ResponseData) result;
        }
        if (responseData == null) {
            return false;
        }
        String retCode = String.valueOf(responseData.getCode());
        return (retCode.equals(logCode));
    }

    /**
     * 参数拼装
     */
    private Object filterParam(Object[] paramsArray) {
        List<Object> paramsList = new ArrayList<>();
        if (paramsArray != null) {
            for (Object item : paramsArray) {
                if (item != null && !isFilterObject(item)) {
                    paramsList.add(item);
                }
            }
        }
        var size = paramsList.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return paramsList.getFirst();
        }
        return paramsList;
    }

    /**
     * 判断是否需要过滤的对象。
     * 比如file、request、response等，不用于日志记录
     *
     * @param obj 对象信息。
     * @return 需要过滤，则返回true；否则false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) obj;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        }
        if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) obj;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return obj instanceof MultipartFile ||
                obj instanceof HttpServletRequest ||
                obj instanceof HttpServletResponse ||
                obj instanceof BindingResult;
    }
}
