package beinet.cn.springbeanstudy.interfaceMulti;

import java.util.List;

/**
 * UsingAll
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 14:32
 */
public class UsingAll {
    private final List<Interface1> implment1List;
    private final List<Interface2> implment2List;

    public UsingAll(List<Interface1> implment1List, List<Interface2> implment2List) {
        this.implment1List = implment1List;
        this.implment2List = implment2List;
    }

    public void execute() {
        System.out.println("Begin Interface1");
        for (Interface1 item : implment1List) {
            item.method1();
        }
        System.out.println("Begin Interface2");
        for (Interface2 item : implment2List) {
            item.method2();
        }
    }
}
