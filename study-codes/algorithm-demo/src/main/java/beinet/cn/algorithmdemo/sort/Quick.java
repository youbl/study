package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，快速排序.
 * 设数组长度为len，x为中间点元素位置，y为对比元素位置：
 * 1、设置x=0，（下面步骤：后面的元素小于x放它左边，大于x放它右边）：
 * 1.1、设置y=len-1：
 * 1.2、比较x与y这两位置的元素大小：
 * 1.2.1、x元素小于y元素时不动，设置y=len-2，循环1.2
 * 1.2.2、x元素大于y元素时，交换x与y的元素，设置y=1，循环1.2
 * 2、完成后，x的值就是中间节点
 * 2.1、对左边队列进行快速排序；
 * 2.2、对右边队列进行快速排序
 * 此算法是不稳定排序。
 * <p>
 * 排序过程举例：待排序元素数组：
 * -30,6,16,-64,-96,-33,-41,-2,-72,91,11,11,
 * 第一轮开始：
 * 中间元素:-30,起始位置:0,结束位置:11
 * -72,6,16,-64,-96,-33,-41,-2,-30,91,11,11,
 * -72,-30,16,-64,-96,-33,-41,-2,6,91,11,11,
 * -72,-41,16,-64,-96,-33,-30,-2,6,91,11,11,
 * -72,-41,-30,-64,-96,-33,16,-2,6,91,11,11,
 * -72,-41,-33,-64,-96,-30,16,-2,6,91,11,11,
 * 第一轮完成，-30左边都小于等于它，右边都大于等于它；
 * <p>
 * 第二轮左边子队列：
 * 中间元素:-72,起始位置:0,结束位置:4
 * -96,-41,-33,-64,-72,-30,16,-2,6,91,11,11,
 * -96,-72,-33,-64,-41,-30,16,-2,6,91,11,11,
 * <p>
 * 中间元素:-33,起始位置:2,结束位置:4
 * -96,-72,-41,-64,-33,-30,16,-2,6,91,11,11,
 * <p>
 * 中间元素:-41,起始位置:2,结束位置:3
 * -96,-72,-64,-41,-33,-30,16,-2,6,91,11,11,
 * 第二轮右边子队列：
 * 中间元素:16,起始位置:6,结束位置:11
 * -96,-72,-64,-41,-33,-30,11,-2,6,91,11,16,
 * -96,-72,-64,-41,-33,-30,11,-2,6,16,11,91,
 * -96,-72,-64,-41,-33,-30,11,-2,6,11,16,91,
 * <p>
 * 中间元素:11,起始位置:6,结束位置:9
 * -96,-72,-64,-41,-33,-30,6,-2,11,11,16,91,
 * <p>
 * 中间元素:6,起始位置:6,结束位置:7
 * -96,-72,-64,-41,-33,-30,-2,6,11,11,16,91,
 */
@Component
public class Quick implements Sort {
    private int loopCount;

    public String getName() {
        return "快速排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;

        //log(source);
        sort(source, 0, source.length - 1);
    }

    private void sort(SortItem[] source, int start, int end) {
        if (start >= end)
            return;
        int midIdx = start;
        int compareIdx = end;
        boolean isDesc = true;

        //System.out.println("中间元素:" + source[midIdx].getNum() + ",起始位置:" + start + ",结束位置:" + end);

        int loop = end - start; // 比较次数
        while (loop > 0 && compareIdx <= end && compareIdx > start) {
            loopCount++;// 循环次数加1

            int compare = source[compareIdx].compareTo(source[midIdx]);
            if ((compare < 0 && isDesc) || (compare > 0 && !isDesc)) {
                swap(source, compareIdx, midIdx);
                //log(source);

                int tmp = midIdx;
                midIdx = compareIdx;
                compareIdx = tmp + (isDesc ? 1 : -1);
                isDesc = !isDesc;
            } else {
                compareIdx += (isDesc ? -1 : 1);
            }
            loop--;
        }

        sort(source, start, midIdx - 1); // 左边快速排序
        sort(source, midIdx + 1, end);   // 右边快速排序
    }

    public int getLoopCount() {
        return loopCount;
    }
}
