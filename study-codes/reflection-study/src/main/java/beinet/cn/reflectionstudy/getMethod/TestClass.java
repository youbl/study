package beinet.cn.reflectionstudy.getMethod;

import java.util.Arrays;

public class TestClass {
    public void test() {
        System.out.println("getMethods:");
        Arrays.stream(ChildClass.class.getMethods()).forEach(method -> System.out.println("  " + method.getName() + " class:" + method.getDeclaringClass().getName()));

        System.out.println("getDeclaredMethods:");
        Arrays.stream(ChildClass.class.getDeclaredMethods()).forEach(method -> System.out.println("  " + method.getName() + " class:" + method.getDeclaringClass().getName()));
    }
}
