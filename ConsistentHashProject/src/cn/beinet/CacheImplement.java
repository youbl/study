package cn.beinet;

public class CacheImplement implements Cache {
    private ConsistentHashRing ring = new ConsistentHashRing();
    private String[] servers;

    public CacheImplement(String[] servers, int virtualNumPerNode) {
        ring.addServers(servers, virtualNumPerNode);
    }

    @Override
    public String get(String key) {
        Cache serverKey = ring.getServer(key);
        return serverKey.get(key);
    }

    @Override
    public void set(String key, String val) {
        Cache serverKey = ring.getServer(key);
        serverKey.set(key, val);
    }

    @Override
    public String toString() {
        return ring.toString();
    }
}