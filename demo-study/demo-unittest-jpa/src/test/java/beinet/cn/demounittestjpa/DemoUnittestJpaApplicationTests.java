package beinet.cn.demounittestjpa;

import beinet.cn.demounittestjpa.entity.Aaa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
@ActiveProfiles("unittest")
class DemoUnittestJpaApplicationTests {


    @Autowired
    DbController controller;

    @Test
    void contextLoads() {
        List<Aaa> arr = controller.getAll();
        Assert.isTrue(arr.isEmpty(), "初始化h2数据库，应该返回空");

        Aaa item = controller.update(12, 10);
        Assert.isTrue(item.getId() == 1, "12不存在，应该插入成功，且id为1");
        Assert.isTrue(item.getNum() == 10, "插入出错？");

        arr = controller.getAll();
        Assert.isTrue(arr.size() == 1, "上面save了 1条记录");

        item = controller.update(12, 24);
        Assert.isTrue(item.getId() == 2, "执行插入");
        Assert.isTrue(item.getNum() == 24, "插入出错？");

        arr = controller.getAll();
        Assert.isTrue(arr.size() == 2, "上面save了 2条记录");


        item = controller.update(1, 36);
        Assert.isTrue(item.getId() == 1, "执行更新");
        Assert.isTrue(item.getNum() == 36, "更新出错？");

        arr = controller.getAll();
        Assert.isTrue(arr.size() == 2, "上面save了 2条记录");
    }

}