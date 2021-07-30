package beinet.cn.springaspectstudy.beans;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class RestControllerAop2 {
    // within(aa.bb.*.rest..*) &&
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllers() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
    )
    public void mappingAnnotations() {
    }

    @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public void requestMappingAnnotations() {
    }

    // 这个不支持RequestMapping
    @Before("restControllers() && requestMappingAnnotations()")
    public void onExecute(JoinPoint thisJoinPoint) {
        System.out.println("\n第二个AOP: " + thisJoinPoint + "\n");
    }

    // 这个全支持了
    @Before("mappingAnnotations()")
    public void onExecute3(JoinPoint thisJoinPoint) {
        System.out.println("\n第三个AOP: " + thisJoinPoint + "\n");
    }
}
