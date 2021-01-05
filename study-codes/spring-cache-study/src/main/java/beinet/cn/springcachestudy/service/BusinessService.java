package beinet.cn.springcachestudy.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * BusinessService
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/4 11:52
 */
@Service
public class BusinessService {
    static final String CACHE_NAME = "cache111";

    // 对返回结果进行缓存, 缓存名为 cache111， 记得要先添加注解 @EnableCaching
    @Cacheable(value = {CACHE_NAME, "xxxx"}) // 会生成2份缓存，一份在cache111，一份在xxxx
    public String getById1(int id) {
        return LocalDateTime.now() + " getById1 " + id;
    }

    // 对返回结果进行缓存, 缓存名为 cache111
    @Cacheable(value = CACHE_NAME)
    public String getById2(int id) {
        return LocalDateTime.now() + " getById2 " + id;
    }


    // 对返回结果进行缓存, 缓存名为 cache111，但是显式指定缓存key, key可以使用属性/方法等
    @Cacheable(value = CACHE_NAME, key = "'get3-' + #id.toString()")
    public String getById3(int id) {
        return LocalDateTime.now() + " getById3 " + id;
    }


    // 使用 keyGenerator指定key，注：keyGenerator的值，是bean的名字
    @Cacheable(value = CACHE_NAME, keyGenerator = "createCacheKeyGener")
    public String getByIdWithKeyGenerator(int id) {
        return LocalDateTime.now() + " getByIdWithKeyGenerator " + id;
    }

    // 使用 keyGenerator 清除缓存，要清除的缓存key，由keyGenerator指定
    @CacheEvict(value = CACHE_NAME, keyGenerator = "createCacheKeyGener")
    public void clearCache(int id) {
        // 空方法就可以了
    }

    @Bean
    CacheKeyGener createCacheKeyGener() {
        return new CacheKeyGener();
    }

    static class CacheKeyGener implements KeyGenerator {
        @Override
        public Object generate(Object target, Method method, Object... params) {
            StringBuilder sb = new StringBuilder();
            for (Object item : params) {
                sb.append(item).append('-');
            }

            if (method.getName().equals("clearCache")) {
                System.out.println("缓存key " + sb + " 缓存被清除");
                // 这里可以做一些其它逻辑，比如发MQ消息，一般不推荐
            } else {
                System.out.println("缓存key " + sb);
            }
            return sb.toString();
        }
    }

    @Cacheable(value = "TenSecondCache")
    public String getAndCache10s(int id) {
        return LocalDateTime.now() + " getAndCache10s " + id;
    }
}
