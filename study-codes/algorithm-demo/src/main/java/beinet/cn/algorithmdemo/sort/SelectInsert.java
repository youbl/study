package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，选择排序.
 * 此算法是稳定排序。
 */
@Component
public class SelectInsert implements Sort {
    private int loopCount;

    public String getName() {
        return "选择排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;

        for (int i = 0, j = source.length; i < j; i++) {
            SortItem item1 = source[i];
            int changeIdx = i;

            for (int m = i + 1; m < j; m++) {
                loopCount++;
                SortItem item2 = source[m];

                if (item1.compareTo(item2) > 0) {
                    item1 = item2;
                    changeIdx = m;
                }
            }
            if (changeIdx != i) {
                SortItem tmp = source[i];
                source[i] = source[changeIdx];
                source[changeIdx] = tmp;
            }
        }
    }

    public int getLoopCount() {
        return loopCount;
    }
}
