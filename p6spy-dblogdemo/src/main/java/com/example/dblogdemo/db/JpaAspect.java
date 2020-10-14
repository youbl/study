package com.example.dblogdemo.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class JpaAspect {
    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository.*(..))")
    //@Pointcut("@within(org.springframework.data.repository.NoRepositoryBean)")
    public void getPointcut() {
    }

    @Around("getPointcut()")
    public Object around1(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAround(joinPoint);
    }


    @Pointcut("@annotation(org.springframework.data.jpa.repository.Query)")
    public void getPointcutQuery() {
    }

    @Around("getPointcutQuery()")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAround(joinPoint);
    }


    private static ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;
        try {
            result = point.proceed();
        } catch (Exception exp) {
            exception = exp;
        }
        long time = System.currentTimeMillis() - beginTime;
        saveLog(point, result, exception, time);

        if (exception != null) {
            throw exception;
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Object result, Exception exception, long time) {
        Dto dto = new Dto();
        dto.setCostTime(time);

        try {
            if (exception != null) {
                dto.setExp(exception);
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            if (result != null) {
                dto.setResult(serial(result));
            }

            //请求的 类名、方法名
            String className = joinPoint.getTarget().getClass().getName();
            String signName = signature.getDeclaringTypeName();
            if (!signName.equalsIgnoreCase(className))
                signName += "|" + className;
            dto.setClas(signName);

            String methodName = signature.getName();
            dto.setMethod(methodName);

            //请求的参数
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                dto.setPara(serial(args));
            }

        } catch (Exception e) {
            dto.setExp(e);
        }

        doLog(dto);
    }

    private void doLog(Dto dto) {
        if (dto.getExp() != null) {
            log.warn(serial(dto));
        } else {
            log.info(serial(dto));
        }
    }

    private static String serial(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return obj.toString();
        }
    }

    @Data
    protected static class Dto {

        /**
         * 调用类名
         */
        private String clas;
        /**
         * 调用方法名
         */
        private String method;
        /**
         * 调用的参数
         */
        private String para;
        /**
         * 方法返回结果
         */
        private String result;
        /**
         * 执行时长，毫秒
         */
        private long costTime;
        /**
         * 备注
         */
        private String remark;

        /**
         * 出现的异常
         */
        private Exception exp;

    }
}
