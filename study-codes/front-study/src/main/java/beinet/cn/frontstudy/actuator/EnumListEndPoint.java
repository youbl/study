package beinet.cn.frontstudy.actuator;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 遍历项目中所有枚举类，并显示的端点类
 *
 * @author youbl
 * @since 2023/04/08
 */
@Endpoint(id = "enums")
@Component
public class EnumListEndPoint {
    @Autowired
    private ApplicationContext context;
    private Map<String, Object> enumsMap;

    /**
     * Endpoint端点所需的get方法
     *
     * @return 枚举类数据列表
     * @throws InvocationTargetException 可能的异常
     * @throws IllegalAccessException    可能的异常
     */
    @ReadOperation
    public Map<String, Object> read() throws InvocationTargetException, IllegalAccessException {
        if (enumsMap == null) {
            synchronized (this) {
                if (enumsMap == null)
                    enumsMap = Init();
            }
        }
        return enumsMap;
    }

    private Map<String, Object> Init() throws IllegalAccessException, InvocationTargetException {
        Class<?> appClass = getSpringBootClass();
        if (appClass == null)
            return new HashMap<>();

        String packageName = getScanPackageName(appClass);
        Set<Class<? extends Enum>> enums = getEnumsFromPackage(packageName);

        Map<String, Object> map = new HashMap<>();
        for (Class enumClass : enums) {
            EnumObject enumObject = new EnumObject();
            enumObject.Description = getDescription(enumClass);

            map.put(enumClass.getTypeName(), enumObject);

            List<Method> methods = getPropertyReadMethod(enumClass);
            Field[] values = enumClass.getFields();
            for (Field enumItem : values) {
                if (enumItem.getType() != enumClass)
                    continue;
                enumItem.setAccessible(true);
                Object enumValue = enumItem.get(null);
                String code = enumItem.getName();

                enumObject.Enums.put(code, getMap(enumValue, methods));
            }
        }
        return map;
    }

    /**
     * 获取SpringBoot项目的主类
     *
     * @return 主类
     */
    private Class<?> getSpringBootClass() {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SpringBootApplication.class);
        if (beansWithAnnotation.isEmpty()) {
            return null;
        }
        return beansWithAnnotation.values().toArray()[0].getClass();
    }

    /**
     * 获取指定包下的所有Enum类
     *
     * @param packageName 包名
     * @return Enum类清单
     */
    private Set<Class<? extends Enum>> getEnumsFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(Enum.class);
    }

    /**
     * 获取指定类型的属性get方法
     *
     * @param clazz 类型
     * @return getyyif列表
     */
    private List<Method> getPropertyReadMethod(Class clazz) {
        // 方法要求：get开头、公共、非静态、无参数，且忽略getClass和getDeclaringClass
        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().startsWith("get")
                        && Modifier.isPublic(m.getModifiers())
                        && !Modifier.isStatic(m.getModifiers())
                        && m.getParameterCount() == 0)
                .filter(m -> !m.getName().equals("getClass")
                        && !m.getName().equals("getDeclaringClass"))
                .collect(Collectors.toList());

        for (Method method : methods) {
            method.setAccessible(true);
        }
        return methods;
    }

    /**
     * 获取指定类上SpringBootApplication注解定义的scanBasePackages属性，
     * 不存在时，返回类所属的package
     *
     * @param appClass SpringBootApplication注解所在的类
     * @return package包名
     */
    private String getScanPackageName(Class<?> appClass) {
        SpringBootApplication anno = appClass.getAnnotation(SpringBootApplication.class);
        if (anno != null) {
            String[] packages = anno.scanBasePackages();
            if (packages != null && packages.length > 0)
                return packages[0];
        }
        return appClass.getPackage().getName();// .getPackageName();
    }

    /**
     * 获取指定类上，org.springframework.context.annotation.Description的值
     *
     * @param enumClass 枚举类
     * @return Description说明
     */
    private String getDescription(Class enumClass) {
        Annotation description = enumClass.getAnnotation(Description.class);
        if (description == null)
            return "";
        return ((Description) description).value();
    }

    /**
     * 为每个枚举项，获取它的所有属性值，进行填充
     *
     * @param enumInstance enum枚举项
     * @param methods      get方法列表
     * @return 属性名和属性值列表
     * @throws InvocationTargetException 可能的异常
     * @throws IllegalAccessException    可能的异常
     */
    private Map<String, Object> getMap(Object enumInstance, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Method method : methods) {
            String fieldName = method.getName().substring(3); // 移除前面的3个字符，即删除get
            map.put(fieldName, method.invoke(enumInstance)); // 属性名和属性值获取
        }
        return map;
    }

    public static class EnumObject {
        public String Description;
        public Map<String, Map<String, Object>> Enums = new HashMap<>();
    }
}
