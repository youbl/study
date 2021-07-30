package beinet.cn.springaspectstudy.beans;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Aspect
@Component
public class RestControllerAop {
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    public void getPointcut() {
//        // 这里的代码不会执行
//        System.out.println("执行getPointcut");
//    }
//
//    @Around("getPointcut()")
//    public Object doArount(ProceedingJoinPoint pointcut) throws Throwable {
//        System.out.println("执行doArount");
//        return pointcut.proceed();
//    }

    //@Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    @Pointcut(
            "within(@org.springframework.web.bind.annotation.RestController *) && " +
                    "@annotation(requestMapping) && " +
                    "execution(* *(..))"
    )
    public void controller1(RequestMapping requestMapping) {
    }

    @Pointcut(
            "within(@org.springframework.web.bind.annotation.RestController *) && " +
                    "@annotation(requestMapping) && " +
                    "execution(* *(..))"
    )
    public void controller2(PostMapping requestMapping) {
    }

    @Pointcut(
            "within(@org.springframework.web.bind.annotation.RestController *) && " +
                    "@annotation(requestMapping) && " +
                    "execution(* *(..))"
    )
    public void controller3(GetMapping requestMapping) {
    }


    @Before("controller1(requestMapping1)")
    public void advice(JoinPoint thisJoinPoint,
                       RequestMapping requestMapping1) {
        System.out.println("RequestMapping的AOP: " + thisJoinPoint);
        System.out.println("  " + requestMapping1);
        RequestMethod[] methods = requestMapping1.method();
        if (methods.length <= 0)
            System.out.println("  默认值");
        else
            System.out.println("  " + methods[0]);
    }

    @Before("controller2(requestMapping2)")
    public void advice(JoinPoint thisJoinPoint,
                       PostMapping requestMapping2) {
        System.out.println("PostMapping的AOP: " + thisJoinPoint);
        System.out.println("  " + requestMapping2);
    }


    @Before("controller3(requestMapping3)")
    public void advice(JoinPoint thisJoinPoint,
                       GetMapping requestMapping3) {
        System.out.println("GetMapping的AOP: " + thisJoinPoint);
        System.out.println("  " + requestMapping3);
    }
}
