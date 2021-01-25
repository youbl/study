package beinet.cn.springcachestudy1.Bean;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * BeinetCacheResolver
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/23 16:55
 */
@Component("beinetCacheResolver")
public class BeinetCacheResolver implements CacheResolver {
    private CacheManager cacheManager;
    private Environment env;

    public BeinetCacheResolver(CacheManager cacheManager, Environment env) {
        this.cacheManager = cacheManager;
        this.env = env;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        CacheOperation operation = (CacheOperation) context.getOperation();
        Set<Cache> resolvedCaches = new HashSet<>();
        Set<String> names = operation.getCacheNames();
        for (String name : names) {
            String resolvedName = env.resolvePlaceholders(name); // 解析里面的占位符 ${xx}
            resolvedCaches.add(cacheManager.getCache(resolvedName));
        }
        return resolvedCaches;
    }
}
