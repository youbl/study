package beinet.cn.springcachestudy.service;

import beinet.cn.springcachestudy.cache.BeinetCacheEnv;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * CustomResolverService
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/13 14:21
 */
@Service
@CacheConfig(cacheResolver = BeinetCacheEnv.RESOLVER_NAME, cacheManager = "ConcurrentMapCacheManager")
public class CustomResolverService {

    @Cacheable(key = "'dto:' + #id.toString() + ':' + #name")
    public DtoDemo get1(int id, String name) {
        return DtoDemo.builder()
                .id(id)
                .name(name)
                .birthday(LocalDateTime.of(2012, 11, 30, 23, 59, 58))
                .age(23)
                .creationTime(LocalDateTime.now())
                .build();
    }


    @Cacheable(key = "'dto:' + #id.toString() + ':' + #name")
    public DtoDemo get2(int id, String name) {
        return DtoDemo.builder()
                .id(id)
                .name(name)
                .build();
    }
}
