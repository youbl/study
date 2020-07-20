package cn.beinet.dto;

import lombok.Data;
import lombok.Synchronized;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Data
public class TestOutputDto {
    /**
     * 收集所有的请求时长
     */
    private SortedMap<Long, Long> map = new TreeMap<>();

    @Synchronized
    public void putTime(long time) {
        map.put(time, 0L);
    }

    @Override
    public String toString() {
        long totalTime = 0; // 总的请求时长
        long time95 = 0;    // 95分位请求时长
        long begin95 = map.size() * 95 / 100; // 95分位请求所在的索引位置
        long idx = 0;
        for (Map.Entry<Long, Long> item : map.entrySet()) {
            if (idx == begin95)
                time95 = item.getKey();
            totalTime += item.getKey();
            idx++;
        }
        long avg = totalTime / map.size();

        return String.format("压测结果：平均响应时间: %s纳秒 95分位响应时间: %s纳秒", avg, time95);
    }
}
