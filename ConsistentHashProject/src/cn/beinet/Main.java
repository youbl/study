package cn.beinet;

public class Main {
    static String[] servers = new String[]{
            "10.0.0.1",
            "10.0.0.2",
            "10.0.0.3",
            "10.0.0.4",
            "10.0.0.5",
            "10.0.0.6",
            "10.0.0.7",
            "10.0.0.8",
            "10.0.0.9",
            "10.0.0.10",
    };
    static int maxCacheNum = 1000000;

    public static void main(String[] args) {
        testCache(0);

        testCache(1);

        testCache(10);

        testCache(100);
    }

    static void testCache(int virtualNumPerNode) {
        Cache cache = new CacheImplement(servers, virtualNumPerNode);
        fillCache(cache, maxCacheNum);
        System.out.println(cache);
    }

    static void fillCache(Cache cache, int cacheNum) {
        for (int i = 0; i < cacheNum; i++) {
            String key = "key" + i;
            //System.out.println(String.valueOf(i) + ":" + (ConsistentHashRing.getHashCode(key)));
            String val = "val" + key;
            cache.set(key, val);
        }
    }
}