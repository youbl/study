package beinet.cn.frontstudy.actuator;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

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
    private Map<String, Object> enumMap;

    private void Init() throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SpringBootApplication.class);
        if (!beansWithAnnotation.isEmpty()) {
            Class<?> appClass = beansWithAnnotation.values().toArray()[0].getClass();
            Reflections reflections = new Reflections(getScanPackageName(appClass));
            Set<Class<? extends Enum>> enums = reflections.getSubTypesOf(Enum.class);
            for (Class<? extends Enum> enumClass : enums) {
                List<Method> methods = Arrays.stream(enumClass.getMethods())
                        .filter(m -> m.getName().startsWith("get") && Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 0)
                        .filter(m -> !m.getName().equals("getClass") && !m.getName().equals("getDeclaringClass"))
                        .collect(Collectors.toList());
                for (Method method : methods) {
                    method.setAccessible(true);
                }

                EnumObject enumObject = new EnumObject();
                enumObject.Description = getDescription(enumClass);

                if (enumMap == null)
                    enumMap = new HashMap<>();
                enumMap.put(enumClass.getTypeName(), enumObject);
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
        }
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
    private String getDescription(Class<? extends Enum> enumClass) {
        Description description = enumClass.getAnnotation(Description.class);
        if (description == null)
            return "";
        return description.value();
    }

    @ReadOperation
    public Map<String, Object> read() throws InvocationTargetException, IllegalAccessException {
        if (enumMap == null) {
            synchronized (this) {
                if (enumMap == null)
                    Init();
                if (enumMap == null)
                    enumMap = new HashMap<>();
            }
        }
        return enumMap;
    }

    private Map<String, Object> getMap(Object enumInstance, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Method method : methods) {
            map.put(method.getName().substring(3), method.invoke(enumInstance));
        }
        return map;
    }

    public static class EnumObject {
        public String Description;
        public Map<String, Map<String, Object>> Enums = new HashMap<>();
    }
}
