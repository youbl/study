package beinet.cn.reflectionstudy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class ReflectionStudyApplication implements CommandLineRunner {
    @Autowired
    ObjectMapper mapper;

    public static void main(String[] args) {
        SpringApplication.run(ReflectionStudyApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        showAllMethodInfo();
    }

    void showAllMethodInfo() {
        for (Method method : getMethods()) {
            showMethodInfo(method);
        }
    }

    void showMethodInfo(Method method) {
        if (method.getName().equals("hashCode") ||
                method.getName().equals("toString") ||
                method.getName().equals("notify") ||
                method.getName().equals("notifyAll") ||
                method.getName().equals("getClass") ||
                method.getName().equals("wait") ||
                method.getName().equals("equals")) {
            return;
        }
        Class<?> clzz = method.getReturnType();
        System.out.println(method + "\n  返回类型: " + clzz + " 数组:" + clzz.isArray()
                + "\n  组件类型: " + clzz.getComponentType()
                + "\n  泛型类型: " + method.getGenericReturnType());
    }

    List<Method> getMethods() {
        return Arrays.stream(SomeMethod.class.getMethods()).sorted(Comparator.comparing(Method::getName)).collect(Collectors.toList());
    }

}
