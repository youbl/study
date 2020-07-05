package cn.beinet;

public interface Cache {
    /**
     * 根据key读取缓存数据
     * @param key key
     * @return 缓存
     */
    String get(String key);

    /**
     * 设置缓存
     * @param key 缓存key
     * @param val 缓存值
     */
    void set(String key, String val);
}
