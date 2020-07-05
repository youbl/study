package cn.beinet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希环类
 */
public class ConsistentHashRing {
    TreeMap<Integer, Cache> map = new TreeMap<>();
    List<Cache> servers = new ArrayList<>();

    /**
     * 往环中加入服务器
     *
     * @param servers           要加入的服务器列表
     * @param virtualNumPerNode 每台服务器增加多少个虚拟节点，0表示不增加虚拟节点
     */
    public void addServers(String[] servers, int virtualNumPerNode) {
        if (servers != null) {
            for (String server : servers) {
                Cache cacheServer = new CacheServerMock(server);
                map.put(getHashCode(server), cacheServer);

                // 加虚拟节点
                for (int i = 1; i <= virtualNumPerNode; i++) {
                    map.put(getVirutalHashCode(server, i), cacheServer);
                }

                this.servers.add(cacheServer);
            }
        }
    }

    /**
     * 根据指定的缓存key，查找环上的对应服务器返回
     *
     * @param cacheKey 缓存key
     * @return 服务器
     */
    public Cache getServer(String cacheKey) {
        if (map.size() <= 0)
            throw new IllegalArgumentException("you must add server before call this method.");
        if (cacheKey == null || cacheKey.isEmpty())
            throw new IllegalArgumentException("cacheKey can't be empty");

        int hash = getHashCode(cacheKey);
        Map.Entry<Integer, Cache> server = map.ceilingEntry(hash);
        if (server == null)
            return map.firstEntry().getValue();
        return server.getValue();
    }

    /**
     * 返回第i个虚拟结点的hash值，注意不能用随机值，以免重启导致变化。
     *
     * @param key 服务器值
     * @param i   第几个虚拟节点
     * @return hash code
     */
    private int getVirutalHashCode(String key, int i) {
        key = key + "&&Virtual" + i;
        return getHashCode(key);
    }

    public static int getHashCode(String key) {
        // int ret = key.hashCode(); // 结果太聚集，不使用
        final int p = 16777619;
        int ret = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            ret = (ret ^ key.charAt(i)) * p;
        }
        ret += ret << 13;
        ret ^= ret >> 7;
        ret += ret << 3;
        ret ^= ret >> 17;
        ret += ret << 5;

        return ret < 0 ? -ret : ret;
    }

    @Override
    public String toString() {
        int totalNum = 0;
        for (Cache cache : servers) {
            CacheServerMock mock = (CacheServerMock) cache;
            totalNum += mock.getCaches().size();
        }
        int avg = totalNum / servers.size();
        StringBuilder sb = new StringBuilder();
        sb.append("总缓存个数：").append(totalNum)
                .append(" 服务器台数：").append(servers.size())
                .append(" 每服务器虚拟节点数：").append(map.size()/servers.size() - 1)
                .append(" 平均值：").append(avg)
                .append("\r\n");

        // 标准差公式： (每个样本 - 平均值)的平方 / (服务器数 - 1)，结果再取平方根
        long diff = 0;
        for (Cache cache : servers) {
            CacheServerMock mock = (CacheServerMock) cache;
            int size = mock.getCaches().size();
            sb.append("Server-Name:")
                    .append(mock.getServerName())
                    .append(" Cache-num: ")
                    .append(size)
                    .append("\r\n");
            long tmp = size - avg;
            diff += (tmp * tmp);
        }
        diff /= (servers.size() - 1);
        sb.append("标准差为： ").append(Math.sqrt(diff)).append("\r\n");
        return sb.toString();
    }
}