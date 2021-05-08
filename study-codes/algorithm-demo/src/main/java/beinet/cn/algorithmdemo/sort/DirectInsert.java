package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;
import org.springframework.stereotype.Component;

/**
 * 从小到大，直接插入排序.
 * 根据返回结果的originIdx顺序，
 * 可知直接插入排序，是稳定排序。
 */
@Component
public class DirectInsert implements Sort {
    /**
     * 直接在源数组上排序
     *
     * @param source 排序前的数组
     */
    public void sort(SortItem[] source) {
        // 从第2个元素开始遍历，在左边找它的插入位置
        for (int i = 1, j = source.length; i < j; i++) {
            SortItem item = source[i];
            int position = findPosition(item, source, i - 1);
            insertNum(item, source, position, i);
        }
    }

    /**
     * 从0到maxPosition，查找num应该存放的位置
     *
     * @param item        用于查找的元素
     * @param source      查找的数据数组
     * @param maxPosition 用于比对的最大位置
     * @return 位置
     */
    private int findPosition(SortItem item, SortItem[] source, int maxPosition) {
        // 在该元素的左边，从右往左遍历
        for (int m = maxPosition; m >= 0; m--) {
            if (source[m].getNum() <= item.getNum())
                return m + 1;
        }
        return 0;
    }

    /**
     * 把num插入到数组的指定位置
     *
     * @param item            待插入数据
     * @param source          数组
     * @param position        要插入的位置
     * @param currentPosition num当前所在位置
     */
    private void insertNum(SortItem item, SortItem[] source, int position, int currentPosition) {
        if (position == currentPosition)
            return;
        // 逐个右移
        for (int i = currentPosition - 1; i >= position; i--) {
            source[i + 1] = source[i];
        }
        // 插入
        source[position] = item;
    }
}
