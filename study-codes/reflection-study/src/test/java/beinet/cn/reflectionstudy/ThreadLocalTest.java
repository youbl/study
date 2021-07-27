package beinet.cn.reflectionstudy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.lang.ref.WeakReference;

public class ThreadLocalTest {
    @Test
    public void gcTest() {
        //Dto dto = new Dto(123, "abc"); // 不能用局部变量定义，会有强引用
        WeakReference weak = new WeakReference<>(new Dto(123, "abc"));
        output(weak);
        Assert.notNull(weak.get(), "不可能");
        System.gc();
        output(weak);
        Assert.isNull(weak.get(), "不可能");
    }

    void output(WeakReference weak) {
        if (weak == null) {
            System.out.println("为空");
        } else {
            System.out.println(weak + ":" + weak.get());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dto {
        private int id;
        private String name;
    }
}
