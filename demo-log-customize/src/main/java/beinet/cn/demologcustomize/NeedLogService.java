package beinet.cn.demologcustomize;

import beinet.cn.demologcustomize.util.AspectBase;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class NeedLogService extends AspectBase {
    @Pointcut("@annotation(beinet.cn.demologcustomize.NeedLog)")
    public void logPointcut() {
        // do nothing because @Pointcut
    }

    @Around("logPointcut()")
    @Override
    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        return super.logAround(point);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
