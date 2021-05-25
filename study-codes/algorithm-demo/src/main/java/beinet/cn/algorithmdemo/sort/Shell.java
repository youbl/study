package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，希尔排序.
 * 此算法是不稳定排序。
 */
@Component
public class Shell implements Sort {
    private int loopCount;

    public String getName() {
        return "希尔排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;

        // 第一次，每2个作为1组排序；第二次，每4个1组排序；第3次，每8个一组……
        // 9/2=4
        for (int gap = source.length / 2; gap >= 1; gap /= 2) {
            for (int i = gap; i < source.length; i++) {
                for (int j = i - gap; j >= 0 && source[j + gap].compareTo(source[j]) < 0; j -= gap) {
                    loopCount++;
                    swap(source, j, j + gap);
                }
            }
        }
    }

    public int getLoopCount() {
        return loopCount;
    }
}
