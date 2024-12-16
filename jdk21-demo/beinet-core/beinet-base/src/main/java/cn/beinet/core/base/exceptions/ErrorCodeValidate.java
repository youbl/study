package cn.beinet.core.base.exceptions;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 检查错误码是否存在重复定义的类
 */
@Component
@RequiredArgsConstructor
public class ErrorCodeValidate implements CommandLineRunner {
    private final ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        loopAllErrorCodeEnum();
    }

    private void loopAllErrorCodeEnum() {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SpringBootApplication.class);
        if (beansWithAnnotation.isEmpty()) {
            return;
        }
        Class<?> appClass = beansWithAnnotation.values().toArray()[0].getClass();
        Reflections reflections = new Reflections(getScanPackageName(appClass));

        // 直接查找 错误码类型
        Set<Class<? extends BaseErrorEnums>> enums = reflections.getSubTypesOf(BaseErrorEnums.class);
        Map<Integer, BaseErrorEnums> errorCodes = new HashMap<>();
        for (Class<? extends BaseErrorEnums> enumClass : enums) {
            for (BaseErrorEnums value : enumClass.getEnumConstants()) {
                var code = value.getErrorCode();
                var oldVal = errorCodes.putIfAbsent(code, value);
                if (oldVal != null) {
                    throw new RuntimeException(code + " 错误码重复：" + oldVal + " " + value);
                }
            }
        }

        // 查找对应Package下的所有枚举
//        Set<Class<? extends Enum>> enums = reflections.getSubTypesOf(Enum.class);
//        for (Class<? extends Enum> enumClass : enums) {
//            if (!BaseErrorEnums.class.isAssignableFrom(enumClass)) {
//                continue;
//            }
//            System.out.println(enumClass);
//        }
    }


    // 获取 @SpringBootApplication 定义的扫描Package
    private String getScanPackageName(Class<?> appClass) {
        SpringBootApplication anno = appClass.getAnnotation(SpringBootApplication.class);
        if (anno != null) {
            var arr = anno.scanBasePackages();
            if (arr.length > 0)
                return arr[0];
        }
        return appClass.getPackage().getName();// .getPackageName();
    }
}