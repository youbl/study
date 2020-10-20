package beinet.cn.logdemoredis.redis;

import beinet.cn.logdemoredis.redis.util.AspectBase;
import beinet.cn.logdemoredis.redis.util.Dto;
import beinet.cn.logdemoredis.redis.util.ProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 使用自定义代理完成日志输出
 */
@Slf4j
class RedisAdvice extends AspectBase {

    /*
2020-10-19 18:43:15.513 DEBUG 23440 --- [           main] b.cn.logdemoredis.redis.RedisAdvice      : {"clas":"org.springframework.data.redis.connection.lettuce.LettuceConnection","method":"setNX","para":"[\"bbb\",\"\\\"ddd\\\"\"]","result":true,"costTime":0}
2020-10-19 18:43:15.516 DEBUG 23440 --- [           main] b.cn.logdemoredis.redis.RedisAdvice      : {"clas":"org.springframework.data.redis.connection.lettuce.LettuceConnection","method":"setEx","para":"[\"aaa\",60,\"\\\"bbb\\\"\"]","result":true,"costTime":0}
2020-10-19 18:43:15.522 DEBUG 23440 --- [           main] b.cn.logdemoredis.redis.RedisAdvice      : {"clas":"org.springframework.data.redis.connection.lettuce.LettuceConnection","method":"get","para":"[\"aaa\"]","result":"ImJiYiI=","costTime":0}
2020-10-19 18:43:15.543 DEBUG 23440 --- [           main] b.cn.logdemoredis.redis.RedisAdvice      : {"clas":"org.springframework.data.redis.connection.lettuce.LettuceConnection","method":"del","para":"[[\"YmJi\"]]","result":1,"costTime":0}
2020-10-19 18:43:15.546 DEBUG 23440 --- [           main] b.cn.logdemoredis.redis.RedisAdvice      : {"clas":"org.springframework.data.redis.connection.lettuce.LettuceConnection","method":"hGet","para":"[\"abc\",\"def\"]","costTime":0}
     */
    @Override
    protected org.slf4j.Logger getLog() {
        return log;
    }

    // 如果是getConnection方法，把返回结果进行代理包装
    Object interceptorRedisFactory(MethodInvocation invocation) throws Throwable {
        if (!log.isDebugEnabled())
            return invocation.proceed();

        Object ret = invocation.proceed();
        String methodName = invocation.getMethod().getName();
        if (methodName.equals("getConnection")) {
            return ProxyUtils.getProxy(ret, this::interceptorRedis);
        }
        return ret;
    }

    private Object interceptorRedis(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String name = method.getName();
        if (name.equals("isPipelined") || name.equals("close"))
            return invocation.proceed();

        Dto dto = new Dto();
        dto.setMethod(name);

        Object target = invocation.getThis();
        dto.setClas(target.getClass().getName());

        Object[] args = invocation.getArguments();
        dto.setPara(serial(args));

        Object ret = null;
        long start = System.currentTimeMillis();
        try {
            ret = invocation.proceed();
            return ret;
        } catch (Exception exp) {
            dto.setExp(exp);
            throw exp;
        } finally {
            dto.setCostTime(System.currentTimeMillis() - start);
            dto.setResult(ret);
            doLog(dto);
        }
    }
}
