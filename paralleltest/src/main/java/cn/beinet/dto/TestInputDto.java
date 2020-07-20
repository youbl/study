package cn.beinet.dto;

import lombok.Data;

@Data
public class TestInputDto {
    /**
     * 测试地址
     */
    private String url;
    /**
     * 总请求数
     */
    private long requestTime;
    /**
     * 总并发数
     */
    private long concurrencyNum;

    /**
     * 每个线程要请求的次数.
     * 不考虑除不断的情况
     * @return 次数
     */
    public long getTimePerThread() {
        return requestTime / concurrencyNum;
    }

    @Override
    public String toString() {
        return String.format("压测地址: %s 压测次数: %s 并发数: %s", url, requestTime, concurrencyNum);
    }
}
