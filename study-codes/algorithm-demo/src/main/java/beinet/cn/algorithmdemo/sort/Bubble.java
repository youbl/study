package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，冒泡排序.
 * 此算法是稳定排序。
 */
@Component
public class Bubble implements Sort {
    private int loopCount;

    public String getName() {
        return "冒泡排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;

        for (int i = 0, j = source.length; i < j; i++) {
            for (int m = 0; m < j - i - 1; m++) {
                loopCount++; // 循环次数加1
                if (source[m].compareTo(source[m + 1]) > 0) {
                    swap(source, m + 1, m);
                }
            }
        }
    }

    public int getLoopCount() {
        return loopCount;
    }
}
