package beinet.cn.springjpastudy.beans;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Slf4j
public class MultiDataSourceAop {


    @Pointcut("@annotation(beinet.cn.springjpastudy.beans.MySource)")
    public void getPointCut() {
        // 空方法，获取切面用
    }

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
        Class callClass = point.getTarget().getClass();
        return (MySource) callClass.getAnnotation(MySource.class);
    }
}
