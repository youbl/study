package cn.beinet;

/**
 * 单向链表类
 */
public class LinkedList {
    private String data;
    private LinkedList nextNode;

    public LinkedList(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LinkedList getNextNode() {
        return nextNode;
    }

    public void setNextNode(LinkedList nextNode) {
        this.nextNode = nextNode;
    }
}
