package beinet.cn.java11newspecdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 演示java11里比java8，新增的数组特性
 * 主要是增加了 of方法和copyOf方法
 *
 * @author youbl
 * @since 2023/6/7 17:05
 */
@RestController
public class ArraySpecController {
    @GetMapping("List")
    public String ListTest() {
        List<Integer> arr = List.of(123, 456, 789, 123);
        // 返回 java.util.ImmutableCollections$ListN，这是不可修改的类型
        // arr.add(111); // 调用add会抛出异常：java.lang.UnsupportedOperationException
        return arr.getClass().getName() + " 长度:" + arr.size();
    }

    @GetMapping("Map")
    public String MapTest() {
        // of后面的元素不允许重复，会抛异常 java.lang.IllegalArgumentException: duplicate element: key1
        Map<String, Integer> arr = Map.of("key1", 123, "key2", 456);
        // 返回 java.util.ImmutableCollections$MapN，这是不可修改的类型
        // arr.put("key3", 111); // 调用put会抛出异常：java.lang.UnsupportedOperationException
        return arr.getClass().getName() + " 长度:" + arr.size();
    }

    @GetMapping("Set")
    public String SetTest() {
        // of后面的元素不允许重复，会抛异常 java.lang.IllegalArgumentException: duplicate element: 123
        Set<Integer> arr = Set.of(123, 456);
        // 返回 java.util.ImmutableCollections$MapN，这是不可修改的类型
        // arr.add(111); // 调用add会抛出异常：java.lang.UnsupportedOperationException

        //var arr2 = Set.copyOf(List.of(123, 456, 789, 123));
        var arr2 = Set.copyOf(arr); // 不可修改类型，通过copyOf返回它自身的引用
        System.out.println(arr2 == arr);
        return arr.getClass().getName() + " 长度:" + arr.size();
    }
}
