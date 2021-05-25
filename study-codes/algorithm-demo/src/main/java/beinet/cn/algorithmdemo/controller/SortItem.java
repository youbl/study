package beinet.cn.algorithmdemo.controller;

import lombok.Builder;
import lombok.Data;

/**
 * 用于排序的对象
 */
@Data
@Builder
public class SortItem implements Comparable<SortItem> {
    /**
     * 排序用字段
     */
    private int num;
    /**
     * 原始序号
     */
    private int originIdx;

    /**
     * 用于排序的对比接口方法
     * @param o
     * @return
     */
    @Override
    public int compareTo(SortItem o) {
        return this.getNum() - o.getNum();
    }
}
