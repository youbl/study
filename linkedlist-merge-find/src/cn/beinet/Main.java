package cn.beinet;

public class Main {

    public static void main(String[] args) {
        // 数据初始化
        LinkedList listM = initListM();
        LinkedList listN = initListN();
        // 添加合并结点，注释掉这一句，就可以测试无合并的场景
        appendSameNode(listM, listN);

        // 开始查找
        LinkedList mergeNode = findMergeNode(listM, listN);
        if (mergeNode == null)
            System.out.println("不存在合并结点");
        else
            System.out.println("2个链表合并于结点： " + mergeNode.getData());
    }

    /**
     * 查找2个单向链表的合并结点，无合并时，返回null
     *
     * @param listM 链表m
     * @param listN 链表n
     * @return
     */
    private static LinkedList findMergeNode(LinkedList listM, LinkedList listN) {
        int m = getLen(listM);
        int n = getLen(listN);

        // 交换，确保listM是较大的链表
        if (m < n) {
            LinkedList tmp = listM;
            listM = listN;
            listN = listM;
            int tmpNum = m;
            m = n;
            n = tmpNum;
        }
        System.out.println("链表m长度 " + m + ", 链表n长度 " + n);
        listM = jumpToSameNum(listM, m - n); // m链表先遍历，确保m链表剩下的结点数 跟n链表一致

        for (int i = 0; i < n; i++) {
            if (listM.equals(listN))
                return listM;
            listM = listM.getNextNode();
            listN = listN.getNextNode();
        }
        return null;
    }

    private static int getLen(LinkedList list) {
        int ret = 1;
        while (list.getNextNode() != null) {
            ret++;
            list = list.getNextNode();
        }
        return ret;
    }

    // 大的链表，先遍历掉前面的结点，以便跟小链表同步
    private static LinkedList jumpToSameNum(LinkedList list, int jumpNum) {
        for (int i = 0; i < jumpNum; i++)
            list = list.getNextNode();
        return list;
    }

    private static LinkedList initListM() {
        LinkedList head = new LinkedList("d");

        LinkedList node1 = new LinkedList("e");
        head.setNextNode(node1);

        LinkedList node2 = new LinkedList("f");
        node1.setNextNode(node2);

        return head;
    }

    private static LinkedList initListN() {
        LinkedList head = new LinkedList("a");
        LinkedList node = new LinkedList("b");
        head.setNextNode(node);

        return head;
    }

    // 添加相同结点
    private static void appendSameNode(LinkedList listM, LinkedList listN) {
        while (listM.getNextNode() != null)
            listM = listM.getNextNode();
        while (listN.getNextNode() != null)
            listN = listN.getNextNode();

        LinkedList node1 = new LinkedList("x");
        listM.setNextNode(node1);
        listN.setNextNode(node1);

        LinkedList node2 = new LinkedList("y");
        node1.setNextNode(node2);

        LinkedList node3 = new LinkedList("z");
        node2.setNextNode(node3);
    }
}
