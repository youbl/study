package beinet.cn.reflectionstudy.getMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestClass {
    public void test() {
        System.out.println("getMethods:");
        Arrays.stream(ChildClass.class.getMethods()).forEach(method -> System.out.println("  " + method.getName() + " class:" + method.getDeclaringClass().getName()));

        System.out.println("getDeclaredMethods:");
        Arrays.stream(ChildClass.class.getDeclaredMethods()).forEach(method -> System.out.println("  " + method.getName() + " class:" + method.getDeclaringClass().getName()));
    }

    public void testGetClass() {
        // 没有被子类override的方法，getDeclaringClass是父类，实际使用中可能导致一些问题，比如获取不到子类的注解，要注意一下
        System.out.println("getMethods:");
        for (Method method : ChildClass.class.getMethods()) {
            System.out.printf("  方法:%s\n    %s\n", method.getName(), method.getDeclaringClass().getName());
        }
        System.out.println("getDeclaredMethods:");
        for (Method method : ChildClass.class.getDeclaredMethods()) {
            System.out.printf("  方法:%s\n    %s\n", method.getName(), method.getDeclaringClass().getName());
        }
    }

}
