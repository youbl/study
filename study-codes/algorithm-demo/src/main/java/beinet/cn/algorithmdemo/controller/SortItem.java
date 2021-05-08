package beinet.cn.algorithmdemo.controller;

import lombok.Builder;
import lombok.Data;

/**
 * 用于排序的对象
 */
@Data
@Builder
public class SortItem {
    /**
     * 排序用字段
     */
    private int num;
    /**
     * 原始序号
     */
    private int originIdx;
}
