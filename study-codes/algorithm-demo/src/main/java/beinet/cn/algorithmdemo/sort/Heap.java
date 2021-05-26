package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，堆排序.
 * 此算法是不稳定排序。
 * 堆：是完全二叉树。
 * 大顶堆：每个结点都大于等于左右子结点。
 * 小顶堆：每个结点都小于等于左右子结点。
 * 一维数组存堆：元素0为根结点，元素1,2为第1层子结点，元素3,4,5,6为第2层子结点……
 * 设数组长为n，索引从0开始，那么：
 * 结点i的左子结点为2i+1，右子结点为2i+2.
 * len/2-1 为最后一个非叶子结点（有孩子的结点）
 */
@Component
public class Heap implements Sort {
    private int loopCount;

    public String getName() {
        return "堆排序";
    }

    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        loopCount = 0;

        // 构建大顶堆
        for (int i = source.length / 2 - 1; i >= 0; i--) {
            adjustHeap(source, i, source.length);
        }
        //log(source);// 打个日志，看看完成的大顶堆

        // 构建完成，开始排序过程：
        // 1、把堆顶与堆尾交换，使最大元素在末尾
        // 2、除了最后一个元素，剩下的重新构建堆
        // 3、把堆顶与堆尾交换（此时的堆尾是倒数第2个元素了）
        // 4、除了最后2个元素，剩下的重新构建堆，如此类似
        for (int i = source.length - 1; i > 0; i--) {
            swap(source, i, 0);

            adjustHeap(source, 0, i);
        }

    }

    /**
     * 设置大顶堆
     *
     * @param source 用于设置大顶堆的数组
     * @param start  根结点位置
     * @param length 整个完整堆的数组长度
     */
    private void adjustHeap(SortItem[] source, int start, int length) {
        // i = start * 2 + 1 定位到左子结点,下一个循环就是遍历子树
        for (int i = start * 2 + 1; i < length; i = 2 * i + 1) {
            loopCount++;// 循环次数加1

            // 取左右子树中，值较大的那个节点，用于与根结点 比较和交换
            if (i + 1 < length && source[i].compareTo(source[i + 1]) < 0) {
                i++;
            }

            if (source[i].compareTo(source[start]) <= 0) {
                break; // 子结点小于等于根结点，不用交换，退出循环
            }

            // 子结点大于根节点时，交换; 并继续循环子结点，因为交换后会影响子树
            swap(source, i, start);
            start = i;
        }
    }

    public int getLoopCount() {
        return loopCount;
    }

}
