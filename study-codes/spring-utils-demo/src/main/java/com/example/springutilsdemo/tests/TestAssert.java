package com.example.springutilsdemo.tests;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/7/5 14:10
 */
@RestController
public class TestAssert {

    @GetMapping("assert1")
    public String assertTest1(@RequestParam(required = false) String str) {
        // assert关键字，默认情况下此语句无效，必须在VM启动参数中开启：-enableassertions 或者 -ea 才会生效
        assert str != null;

        // 可以使用 org.springframework.util.Assert 来取代assert关键字
        Assert.notNull(str, "str参数不能为空");
        return str + "---" + LocalDateTime.now();
    }


    @GetMapping("assert2")
    public String assertTest2(@RequestParam String str, @RequestParam int flg) {
        switch (flg) {
            case 0:
                Assert.doesNotContain(str, "abc", "字符串不允许包含abc");
                break;
            case 1:
                // 用方法是避免不必要的数据库或url获取
                Assert.doesNotContain(str, "abc", () -> {
                    return "不允许包含abc时，从数据库或url获取错误信息";
                });
                break;

            case 2:
                Assert.hasLength(str, "str为空或长度为0了");
                // Assert.hasLength(str, ()->{数据库操作或url远程获取});
                break;

            case 3:
                Assert.hasText(str, "str为空或长度为0或全部为空格");
                // Assert.hasText(str, ()->{数据库操作或url远程获取});
                break;

            case 4:
                Class<?> subType = str.getClass();
                Class<?> superType = Object.class;
                // 如果 subType不是superType的子类，则抛出异常
                Assert.isAssignable(superType, subType, () -> subType.getName() + "不是" + superType.getName() + "的子类");
                break;

            case 5:
                Class<?> superType2 = Object.class;
                // 如果 str不是superType的实例，则抛出异常
                Assert.isInstanceOf(superType2, str, "str的类型不符-" + superType2.getName());
                break;

            case 6:
                Assert.isNull(str, "str不为null要报错");
                break;

            case 7:
                String[] arr = new String[]{"", null, "123"};
                Assert.noNullElements(arr, "数组里不能有null元素");
                break;

            case 8:
                String[] arr2 = new String[]{"", null, "123"};
                Assert.notEmpty(arr2, "数组不能为null或没有元素");
                break;

            case 9:
                // 等同于Assert.isTrue，只是抛出的异常不同
                Assert.state(str == null, "数组不能为null或没有元素");
                break;
        }

        return str;
    }
}
