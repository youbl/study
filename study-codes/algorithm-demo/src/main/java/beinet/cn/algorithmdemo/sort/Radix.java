package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，基数排序.
 * 原理：
 * 1、建10个桶，元素的个位为0，放入第0个桶，个位为1放入第1个桶……
 * 2、从第0个桶开始，把数组重新串起来，拼成新的数组；
 * 3、清空桶，然后按十位放入桶，十位为0放入第0个桶……
 * 4、串起来
 * 5、清空桶，按百位，以此类推
 * 注意：此排序不支持负数，本代码里会找出最小负数，并转0处理
 * 此算法是不稳定排序。
 */
@Component
public class Radix implements Sort {
    private int loopCount;

    public String getName() {
        return "基数排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;
        // 消除负数，注意可能会溢出
        int minNum = getAndAddToPositiveNum(source);

        doSort(source);

        // 恢复负数
        if (minNum < 0) {
            for (SortItem item : source) {
                item.setNum(item.getNum() + minNum);
            }
        }
    }

    private void doSort(SortItem[] source) {
        SortItem[][] burst = new SortItem[10][source.length]; // 初始化桶，每个桶最大容量就是数组长度，防止数组个位全是9这种情况
        int[] burstLen = new int[10];// 每个桶的实际数据长度

        // 从10^0循环到10^100次方，暂不考虑超出的场景
        for (int radix = 0; radix < 100; radix++) {
            int radix10 = (int) Math.pow(10, radix);
            boolean allLow = true;// 为true表示都小于这个10^radix，可以退出了

            // 加入桶
            for (int i = 0, j = source.length; i < j; i++) {
                int numForBurst = source[i].getNum() / radix10;
                if (numForBurst > 0)
                    allLow = false;
                int num = numForBurst % 10;
                int currentPos = burstLen[num];
                burst[num][currentPos] = source[i];
                burstLen[num]++;

                loopCount++;// 循环次数加1
            }
            if (allLow) {
                break;
            }
            // 串成新数组
            for (int i = 0, j = source.length; i < j; ) {
                for (int m = 0; m < 10; m++) {
                    for (int n = 0; n < burstLen[m]; n++) {
                        source[i] = burst[m][n];
                        i++;
                        loopCount++;// 循环次数加1
                    }
                    burstLen[m] = 0; // 串完数据，长度要重置
                }
            }
            //log(source);
        }
    }

    /**
     * 找出最小的负数返回，同时数组每个数都减这个负数，确保数组都是大于等于0的数
     */
    private int getAndAddToPositiveNum(SortItem[] source) {
        int minNum = 0;
        for (int i = 0, j = source.length; i < j; i++) {
            if (source[i].getNum() < minNum)
                minNum = source[i].getNum();
        }
        if (minNum >= 0) {
            return 0;// 都不是负数，不需要处理
        }
        for (SortItem item : source) {
            item.setNum(item.getNum() - minNum);
        }
        return minNum;
    }

    public int getLoopCount() {
        return loopCount;
    }
}
