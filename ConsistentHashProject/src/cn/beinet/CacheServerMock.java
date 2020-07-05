package cn.beinet;

import java.util.HashMap;
import java.util.Map;

/**
 * 具体缓存服务器通讯类，这里用Mock类代替
 */
public class CacheServerMock implements Cache {
    private Map<String, String> caches = new HashMap<>();

    private String serverName;

    public CacheServerMock(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public Map<String, String> getCaches() {
        return caches;
    }

    @Override
    public String get(String key) {
        return caches.get(key);
    }

    @Override
    public void set(String key, String val) {
        caches.put(key, val);
    }
}