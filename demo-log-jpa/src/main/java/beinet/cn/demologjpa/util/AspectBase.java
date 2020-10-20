package beinet.cn.demologjpa.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public abstract class AspectBase {

    protected static ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

    protected abstract org.slf4j.Logger getLog();


    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;
        try {
            result = point.proceed();
        } catch (Exception exp) {
            exception = exp;
        }
        long time = System.currentTimeMillis() - beginTime;
        saveLog(point, result, exception, time);

        if (exception != null) {
            throw exception;
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Object result, Exception exception, long time) {
        Dto dto = new Dto();
        dto.setCostTime(time);

        try {
            if (exception != null) {
                dto.setExp(exception);
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            if (result != null) {
                dto.setResult(result);
            }

            //请求的 类名、方法名
            String className = joinPoint.getTarget().getClass().getName();
            String signName = signature.getDeclaringTypeName();
            if (!signName.equalsIgnoreCase(className))
                signName += "|" + className;
            dto.setClas(signName);

            String methodName = signature.getName();
            dto.setMethod(methodName);

            //请求的参数
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                dto.setPara(serial(args));
            }

        } catch (Exception e) {
            dto.setExp(e);
        }

        doLog(dto);
    }


    protected void doLog(Dto dto) {
        if (dto.getExp() != null) {
            getLog().warn(serial(dto));
        } else {
            getLog().debug(serial(dto));
        }
    }

    protected String serial(Object[] obj) {
        if (obj == null || obj.length <= 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Object item : obj) {
            if (sb.length() > 0)
                sb.append(",");
            sb.append(serial(item));
        }
        sb.insert(0, "[").append("]");
        return sb.toString();
    }

    protected String serial(Object obj) {
        if (obj == null)
            return "";
        try {
            if (obj instanceof byte[]) {
                obj = new String((byte[]) obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            getLog().error(String.format("%s serialize error: %s", obj.getClass().getName(), ex.toString()));
            return obj.toString();
        }
    }

}
