package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，归并排序.
 * 原理：
 * 1、每2个元素进行排序；
 * 2、第1步结果的每2组（4个元素）进行归并，即按从小到大，放入新数组
 * 3、第2步结果的第2组（8个元素）进行归并，如此类推
 * 4、最后对整个数组进行归并，注意：比如10个元素，是用0~7 与 8~9 这两组归并，不是0~4与5~9
 * 是稳定排序。
 */
@Component
public class Merge implements Sort {
    private int loopCount;

    public String getName() {
        return "归并排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;

        SortItem[] target = new SortItem[source.length]; // 空间复杂度 n
        // 先按2个元素归并，再按4个，再按8个，如此类推
        int i = 2;
        for (int j = source.length; i < j; i *= 2) {
            for (int start = 0; start < j; start += i) {
                //int endLen = j - start;
                //if (endLen > i / 2)                {
                // endLen <= i/2时，表示这个数组长度等于前一次归并，没必要再归并了

                // start是要归并的数组1的第0个元素，mid是数组2的第0个元素
                int mid = start + i / 2;
                sort(source, start, mid, target);
                //}
            }

            SortItem[] tmp = source;
            source = target;
            target = tmp; // 直接复用source的空间
        }
        // 最后做一次全数组归并
        sort(source, 0, i / 2, target);

        // 把target赋值过来
        for (int idx = 0, j = source.length; idx < j; idx++) {
            loopCount++;// 循环次数加1
            source[idx] = target[idx];
        }
        log(source);
    }

    private void sort(SortItem[] source, int start, int mid, SortItem[] target) {
        int place = start;  // 元素要放入target的起始位置
        int startArr1 = start;
        int startArr2 = mid;
        int endArr2 = startArr2 + mid - start - 1;// 数组2的最后一个元素；mid-1是数组1的最后一个元素

        for (; startArr1 < mid && startArr2 <= endArr2 && startArr2 < source.length; ) {
            loopCount++;// 循环次数加1

            int compare = source[startArr1].compareTo(source[startArr2]);
            if (compare < 0) {
                target[place] = source[startArr1];
                startArr1++;
                place++;
            } else if (compare > 0) {
                target[place] = source[startArr2];
                startArr2++;
                place++;
            } else {
                target[place] = source[startArr1];
                startArr1++;
                place++;
                target[place] = source[startArr2];
                startArr2++;
                place++;
            }
        }

        // 比较完了，把剩余元素放入target
        for (; startArr1 < mid; startArr1++) {
            loopCount++;// 循环次数加1
            target[place] = source[startArr1];
            place++;
        }
        for (; startArr2 <= endArr2 && startArr2 < source.length; startArr2++) {
            loopCount++;// 循环次数加1
            target[place] = source[startArr2];
            place++;
        }
    }

    public int getLoopCount() {
        return loopCount;
    }
}
