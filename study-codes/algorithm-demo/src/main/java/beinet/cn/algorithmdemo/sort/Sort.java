package beinet.cn.algorithmdemo.sort;

import beinet.cn.algorithmdemo.controller.SortItem;

public interface Sort {
    void sort(SortItem[] source);

    int getLoopCount();

    String getName();

    /**
     * 把数组的第i个元素和第j个元素交换
     *
     * @param source 数组
     * @param i      交换元素1
     * @param j      交换元素2
     */
    default void swap(SortItem[] source, int i, int j) {
        if (source[i].compareTo(source[j]) == 0) return;
        SortItem tmp = source[i];
        source[i] = source[j];
        source[j] = tmp;
    }

    default void log(SortItem[] source) {
        for (int i = 0, j = source.length; i < j; i++) {
            System.out.print(source[i].getNum() + ",");
        }
        System.out.println();
    }
}
