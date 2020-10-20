package beinet.cn.demologjpa.jpa;


import beinet.cn.demologjpa.util.AspectBase;
import beinet.cn.demologjpa.util.Dto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 用于JPA的日志服务类
 */
@Aspect
@Slf4j
@Component
public class JpaAop extends AspectBase {

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository.*(..))")
    //@Pointcut("@within(org.springframework.data.repository.NoRepositoryBean)")
    public void getPointcut() {
        // do nothing because @Pointcut
    }

    @Around("getPointcut()")
    public Object around1(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!log.isDebugEnabled()) {
            return joinPoint.proceed();
        }
        return logAround(joinPoint);
    }


    @Pointcut("@annotation(org.springframework.data.jpa.repository.Query)")
    public void getPointcutQuery() {
        // do nothing because @Pointcut
    }

    @Around("getPointcutQuery()")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!log.isDebugEnabled()) {
            return joinPoint.proceed();
        }
        return logAround(joinPoint);
    }

    @Override
    protected void doLog(Dto dto) {
        // 不直接记录结果集，避免太大
        if (dto.getResult() != null && dto.getResult() instanceof Collection) {
            dto.setResult("结果行数:" + ((Collection) dto.getResult()).size());
        }
        if (dto.getExp() != null) {
            log.warn(serial(dto));
        } else {
            log.debug(serial(dto));
        }
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
