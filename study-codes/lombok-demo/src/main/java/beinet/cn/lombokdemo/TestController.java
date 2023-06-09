package beinet.cn.lombokdemo;

import beinet.cn.lombokdemo.demos.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 13:50
 */
@RestController
public class TestController {

    @GetMapping("SneakyThrows")
    public void TestSneakyThrows(@RequestParam(required = false) Integer flg) throws Exception {
        SneakyThrowsDemo demo = new SneakyThrowsDemo();
        if (flg == null || flg <= 1)
            demo.test1();
        else if (flg == 2)
            demo.test2();
        else
            demo.test3();
    }

    @GetMapping("Data")
    public String TestData() {
        DataDemo.Dto1 dto1 = new DataDemo.Dto1();

        // Dto1没有写set和get方法，有Data注解就可以用了
        dto1.setId(123);
        dto1.setName("abc");

        return dto1.getId() + "-" + dto1.getName();
    }

    @GetMapping("ToString")
    public Map<String, String> TestToString() {
        DataDemo.Dto2 dto2 = new DataDemo.Dto2();
        DataDemo.Dto3 dto3 = new DataDemo.Dto3();

        // 默认的toString返回类名，@Data的toString是返回每个字段的key/value
        Map<String, String> ret = new HashMap<>();
        ret.put("lombok.toString", dto2.toString());
        ret.put("Object.toString", dto3.toString());
        return ret;
    }

    @GetMapping("EqualsAndHashCode")
    public Map<String, String> TestEqualsAndHashCode() {
        DataDemo.Dto2 dto2 = new DataDemo.Dto2();
        dto2.setId(123);
        dto2.setName("abc");
        DataDemo.Dto2 dto2Another = new DataDemo.Dto2();
        dto2Another.setId(123);
        dto2Another.setName("abc");

        DataDemo.Dto3 dto3 = new DataDemo.Dto3();
        dto3.setId(123);
        dto3.setName("abc");
        DataDemo.Dto3 dto3Another = new DataDemo.Dto3();
        dto3Another.setId(123);
        dto3Another.setName("abc");

        // 使用了EqualsAndHashCode，且继承父类
        DataDemo.DtoChild1 dtoChild1 = new DataDemo.DtoChild1();
        dtoChild1.setId(11);
        dtoChild1.setName("aaa");
        dtoChild1.setAge(456);
        DataDemo.DtoChild1 dtoChild2 = new DataDemo.DtoChild1();
        dtoChild2.setId(22);
        dtoChild2.setName("bbb");
        dtoChild2.setAge(456);

        // 使用了EqualsAndHashCode(callSuper = true)，且继承父类
        DataDemo.DtoChild2 dtoChildSuper1 = new DataDemo.DtoChild2();
        dtoChildSuper1.setId(11);
        dtoChildSuper1.setName("aaa");
        dtoChildSuper1.setAge(456);
        DataDemo.DtoChild2 dtoChildSuper2 = new DataDemo.DtoChild2();
        dtoChildSuper2.setId(22);
        dtoChildSuper2.setName("bbb");
        dtoChildSuper2.setAge(456);

        Map<String, String> ret = new HashMap<>();
        // lombok中重写了hashCode方法，对key/value全部相同的对象，生成相同的hashCode，并判断equals为相等
        ret.put("lombok.hashCode", dto2.hashCode() + ":" + dto2Another.hashCode());
        ret.put("Object.hashCode", dto3.hashCode() + ":" + dto3Another.hashCode());
        ret.put("lombok.equals", dto2.equals(dto2Another) + "");
        ret.put("Object.equals", dto3.equals(dto3Another) + "");

        // lombok重写的hashCode方法，只对子类属性进行hash计算，这里明明父类属性不同，但是得到的hashCode依旧相同
        // 具体详情可以看DataDemo文件最后的反编译结果，方法的定义
        ret.put("lombok.Child.hashCode", dtoChild1.hashCode() + ":" + dtoChild2.hashCode());
        // 父类属性不相同，但是equals比较结果是true
        ret.put("lombok.Child.equals", dtoChild1.equals(dtoChild2) + "");

        // 加了callSuper = true，就会使用父类属性计算了
        ret.put("lombok.callSuperChild.hashCode", dtoChildSuper1.hashCode() + ":" + dtoChildSuper2.hashCode());
        // 父类属性不相同，equals比较结果也成功变成了false
        ret.put("lombok.callSuperChild.equals", dtoChildSuper1.equals(dtoChildSuper2) + "");

        return ret;
    }


    @GetMapping("Builder")
    public BuilderDemo TestBuilder() {
        // Builder注解默认会添加一个私有的全参数构造函数，不能自行new
        //BuilderDemo result = new BuilderDemo(123, "abc");

        return BuilderDemo.builder()
                .id(123)
                .name("abc")
                .build();
    }

    @GetMapping("Accessors")
    public AccessorsDemo TestAccessors() {
        // 类似于Builder注解，可以直接用set方法进行链式赋值，并返回this对象；但不生成构造函数
        // 建议实际场景中用这个代替Builder
        return new AccessorsDemo()
                .setId(123)
                .setName("abc");
    }

    @GetMapping("Slf4j")
    public void TestSlf4j() {
        // 用了注解，就自动多了一个变量 log
        new Slf4jDemo().doLog();
    }

    @GetMapping("Synchronized")
    public void TestSynchronized() {
        // 这2个线程加锁无效, 因为非静态方法，使用的是实例的成员变量进行lock加锁
        new Thread(() -> {
            new SynchronizedDemo().syncMethod();
        }, "线程1").start();
        new Thread(() -> {
            new SynchronizedDemo().syncMethod();
        }, "线程2").start();


        // 这2个线程使用同一个new的实例，加锁有效，会串行执行
        SynchronizedDemo demo = new SynchronizedDemo();
        new Thread(() -> {
            demo.syncMethod();
        }, "线程3").start();
        new Thread(() -> {
            demo.syncMethod();
        }, "线程4").start();

        // 这2个线程加锁有效，因为静态方法，使用的是静态成员变量进行lock加锁
        new Thread(() -> {
            new SynchronizedDemo().syncStaticMethod();
        }, "线程5").start();
        new Thread(() -> {
            new SynchronizedDemo().syncStaticMethod();
        }, "线程6").start();
    }


    @GetMapping("val")
    public String TestVal() {
        return ValDemo.valDemo();
    }


    @GetMapping("NonNull")
    public String TestNonNull() {
        return new NonNullDemo(null).getField() + "===";
    }

    @GetMapping("NonNull2")
    public String TestNonNull2() {
        return NonNullDemo.nonNullDemo2(null) + "===";
    }


    @GetMapping(value = "Cleanup")
    public void TestCleanup(HttpServletResponse response) {
        CleanupDemo.test(response);
    }

}
