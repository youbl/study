package beinet.cn.springjpastudy.beans;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class MultiDataSourceAop {

//    @Pointcut("@within(beinet.cn.springjpastudy.beans.MySource) || @annotation(beinet.cn.springjpastudy.beans.MySource)")
//    public void beanAnnotatedWithMonitor() {
//    }

//    @Pointcut("execution(public * *(..))")
//    public void publicMethod() {
//    }
//
//    @Pointcut("publicMethod() && beanAnnotatedWithMonitor()")
//    public void getPointCut() {
//    }

//    @Pointcut("@annotation(beinet.cn.springjpastudy.beans.MySource)")
//    public void getPointCut() {
//        // 空方法，获取切面用
//    }

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository.*(..)) || @annotation(org.springframework.data.jpa.repository.Query)")
    public void getPointCut() {
        // 空方法，获取切面用
    }

    //@Before(value = "@within(beinet.cn.springjpastudy.beans.MySource) || @annotation(beinet.cn.springjpastudy.beans.MySource)")
    @Around("getPointCut()")
    public Object doAroundPoint(ProceedingJoinPoint point) throws Throwable {
        setDataSource(point); // 设置数据源
        try {
            Object result = point.proceed();
            return result;
        } finally {
            clearDataSource(); // 清理数据源
        }
    }

    private void setDataSource(ProceedingJoinPoint point) {
        String source = getAnnoSource(point);
        if (!StringUtils.isEmpty(source) && source.equalsIgnoreCase(MultiDataSource.MASTER))
            MultiDataSource.useMaster();
        else
            MultiDataSource.useSlave();
    }

    private void clearDataSource() {
        MultiDataSource.clear();
    }

    private String getAnnoSource(ProceedingJoinPoint point) {
        MySource source = getAnnotation(point);
        if (source == null)
            return "";
        return source.value();
    }

    private MySource getAnnotation(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method callMethod = signature.getMethod();

        // 先获取方法级注解
        MySource annotation = callMethod.getAnnotation(MySource.class);
        if (annotation != null) {
            return annotation;
        }
        // 不存在时，获取类级注解
        // 不能用method.getDeclaringClass()，可能会得到父类，导致出问题
        // 不能用signature.getDeclaringType()，也会得到父类，比如直接拿到JpaRepository
        // point.getTarget().getClass() 会拿到Proxy类
        Class callClass = point.getTarget().getClass();

        if (!callClass.getName().contains("$Proxy"))
            return (MySource) callClass.getAnnotation(MySource.class);

        // 如果是代理类，遍历它的接口，比如JPA
        for (Class clazz : callClass.getInterfaces()) {
            annotation = (MySource) clazz.getAnnotation(MySource.class);
            if (annotation != null)
                return annotation;
        }
        return null;
    }
}
