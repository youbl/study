package beinet.cn.reflectionstudy.returnTypeDemo;

import beinet.cn.reflectionstudy.SomeMethod;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
public class MethodReturnType {
    private final ObjectMapper mapperForOutputWithType;
    private final ObjectMapper mapperForWorkWithoutType;

    public MethodReturnType() {
        this.mapperForOutputWithType = new ObjectMapper();
        this.mapperForOutputWithType.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        this.mapperForWorkWithoutType = new ObjectMapper();
    }

    public void runTest() throws Exception {
        showAllMethodInfo();
        System.out.println();
        testConvert("DtoMethodArray");
        testConvert("DtoMethodList");
        testConvert("LongMethodArray1");
        testConvert("LongMethodArray2");
        testConvert("LongMethodList");
        testConvert("LongMethodMap1");
        testConvert("LongMethodMap2");
        testConvert("LongMethodMap3");
        testConvert("DtoGenericType");
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
        System.out.println(method + "\n  返回类型: " + getTypeStr(clzz)
                + "\n  isArray:" + clzz.isArray()
                + "\n  isCollection:" + Collection.class.isAssignableFrom(clzz)
                + "\n  isMap:" + Map.class.isAssignableFrom(clzz)
                + "\n  ComponentType: " + getTypeStr(clzz.getComponentType())
                + "\n  method.GenericType: " + getTypeStr(method.getGenericReturnType()));
    }

    List<Method> getMethods() {
        return Arrays.stream(SomeMethod.class.getMethods()).sorted(Comparator.comparing(Method::getName)).collect(Collectors.toList());
    }

    String getTypeStr(Type type) {
        if (type == null)
            return "--null--";
        return type + " \t\t【" + type.getClass() + "】";
    }

    void testConvert(String methodName) throws Exception {
        SomeMethod someMethod = new SomeMethod();
        Method method = someMethod.getClass().getMethod(methodName, null);
        Class returnType = method.getReturnType();
        Object obj1 = method.invoke(someMethod);
        Object obj2 = convert(obj1, getReturnType(method));
        System.out.println(returnType + " : " + obj1.getClass() + " : " + obj2.getClass());

        String str1 = mapperForOutputWithType.writeValueAsString(obj1);
        String str2 = mapperForOutputWithType.writeValueAsString(obj2);
        if (!str1.equals(str2)) {
            System.out.println("  ===" + mapperForOutputWithType.writeValueAsString(obj1));
            System.out.println("  ===" + mapperForOutputWithType.writeValueAsString(obj2));
        }
    }

    /**
     * 返回方法的返回值类型，用于反序列化
     *
     * @param method 方法
     * @return 类型
     */
    private Type getReturnType(Method method) {
        Class methodReturnType = method.getReturnType();
        if (methodReturnType.isArray()) {
            // 数组，类似 long[] Long[] 直接返回，不影响反序列化
            return methodReturnType;
        }

        Type type = getCollectionType(method);
        if (type != null) {
            // List HashSet等类型
            return type;
        }
        type = getMapType(method);
        if (type != null) {
            return type;
        }
        return getGenericType(method);
    }

    /**
     * 如果是Collection类型，则返回带泛型的类型
     *
     * @param method 方法
     * @return 类型
     */
    private JavaType getCollectionType(Method method) {
        Class methodReturnType = method.getReturnType();
        if (!Collection.class.isAssignableFrom(methodReturnType)) {
            return null;
        }
        Type type = method.getGenericReturnType();
        if (type != null && type instanceof ParameterizedType) {
            Type[] arrType = ((ParameterizedType) type).getActualTypeArguments();
            if (arrType == null || arrType.length < 1) {
                return null;
            }
            return mapperForWorkWithoutType.getTypeFactory().constructCollectionType(methodReturnType, (Class) arrType[0]);
        }
        return null;
    }

    /**
     * 如果是Map类型，则返回带泛型的类型
     *
     * @param method 方法
     * @return 类型
     */
    private JavaType getMapType(Method method) {
        Class methodReturnType = method.getReturnType();
        if (!Map.class.isAssignableFrom(methodReturnType)) {
            return null;
        }
        Type type = method.getGenericReturnType();
        if (type != null && type instanceof ParameterizedType) {
            Type[] arrType = ((ParameterizedType) type).getActualTypeArguments();
            if (arrType == null || arrType.length < 2) {
                return null;
            }

            return mapperForWorkWithoutType.getTypeFactory().constructMapType(methodReturnType, toClass(arrType[0]), toClass(arrType[1]));
        }
        return null;
    }

    private JavaType toClass(Type type) {
//        if (type instanceof ParameterizedType) {
//        }
        return mapperForWorkWithoutType.getTypeFactory().constructType(type);
    }

    private Type getGenericType(Method method) {
        Type type = method.getGenericReturnType();
        if (type == null) {
            type = method.getReturnType();
        }
        return type;
    }

    /**
     * 把对象转换为指定的类型并返回
     *
     * @param obj        对象
     * @param returnType 类型
     * @return 转换结果
     * @throws JsonProcessingException 异常
     */
    private Object convert(Object obj, Type returnType) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        if (returnType instanceof Class) {
            String serStr = mapperForWorkWithoutType.writeValueAsString(obj);
            return mapperForWorkWithoutType.readValue(serStr, (Class) returnType);
        } else if (returnType instanceof JavaType) {
            String serStr = mapperForWorkWithoutType.writeValueAsString(obj);
            return mapperForWorkWithoutType.readValue(serStr, (JavaType) returnType);
        }
        return obj;
    }
}
