package beinet.cn.springcachestudy.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 此缓存解析器，使用当前应用名作为 cacheName
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/13 14:03
 */
public class BeinetCacheResolver implements CacheResolver {
    private final CacheManager cacheManager;

    @Value("${spring.application.name:UNKNOWN}")
    private String appName;

    public BeinetCacheResolver(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<Cache> cacheList = new ArrayList<>();
        cacheList.add(cacheManager.getCache(appName));
        return cacheList;
    }
}
