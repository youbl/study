package beinet.cn.springrabbitstudy;

import beinet.cn.springrabbitstudy.annotationImp.MyRabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

// 参考实现 https://github.com/spring-projects/spring-amqp/issues/984
public class MyRabbitListenerAnnotationBeanPostProcessor extends RabbitListenerAnnotationBeanPostProcessor {
    public static ApplicationContext Context;

    public MyRabbitListenerAnnotationBeanPostProcessor(ApplicationContext applicationContext) {
        Context = applicationContext;
    }

    @Override
    protected void processAmqpListener(RabbitListener rabbitListener, Method method, Object bean, String beanName) {
        RabbitListener newListener = new MyRabbitListener(rabbitListener);
        super.processAmqpListener(newListener, method, bean, beanName);
    }

    //@Override
    public Object postProcessAfterInitialization111(Object bean, String beanName) throws BeansException {
        //super.postProcessAfterInitialization(bean, beanName);

        final Object metadata = getTypeMetadata(bean);
        final Object listenerMethods = getFieldValue(metadata, "listenerMethods");
        for (int i = 0, j = Array.getLength(listenerMethods); i < j; i++) {
            Object listenerMethod = Array.get(listenerMethods, i);
            Object annotationList = getFieldValue(listenerMethod, "annotations");
            for (int m = 0, n = Array.getLength(annotationList); m < n; m++) {
                RabbitListener rabbitListener = (RabbitListener) Array.get(annotationList, m);

                System.out.println(rabbitListener);
            }
        }
//        for (ListenerMethod lm : metadata.listenerMethods) {
//            for (RabbitListener rabbitListener : lm.annotations) {
//                processAmqpListener(rabbitListener, lm.method, bean, beanName);
//            }
//        }
//        if (metadata.handlerMethods.length > 0) {
//            callMethod("processMultiMethodListeners",
//                    metadata.classAnnotations, metadata.handlerMethods, bean, beanName);
//        }

        return bean;
    }

    Object getTypeMetadata(Object bean) {
        try {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            ConcurrentMap cache = (ConcurrentMap) getFieldValue(this, "typeCache",
                    RabbitListenerAnnotationBeanPostProcessor.class);
            return cache.computeIfAbsent(targetClass,
                    aClass -> callMethod("buildMetadata", aClass));
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    Object getFieldValue(Object obj, String fieldName) {
        return getFieldValue(obj, fieldName, obj.getClass());
    }

    Object getFieldValue(Object obj, String fieldName, Class clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    Object callMethod(String methodName, Object... args) {
        try {
//            StringBuilder sb = new StringBuilder();
//            for (Method mm : RabbitListenerAnnotationBeanPostProcessor.class.getDeclaredMethods()) {
//                sb.append(mm.getName()).append('\n');
//            }
//            System.out.println(sb);

            Class[] argTypes = new Class[args.length];
            int idx = 0;
            for (Object obj : args) {
                argTypes[idx] = obj.getClass();
                idx++;
            }
            Method method = RabbitListenerAnnotationBeanPostProcessor.class.getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return method.invoke(this, args);
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
