package com.example.springutilsdemo.tests;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/7/5 18:22
 */
@RestController
public class TestOthers {
    @GetMapping("ObjectUtils")
    public String testObjectUtils() {
        String[] arr = new String[0];
        // 数组是否为空，支持Array、Collection、Map、字符串、Optional
        System.out.println(ObjectUtils.isEmpty(arr));

        // 添加元素，返回新数组
        arr = ObjectUtils.addObjectToArray(arr, "abc");
        // 转字符串，null返回空，数组则逐一拼接返回
        var strArr = ObjectUtils.nullSafeToString(arr);
        System.out.println(strArr + "  长度:" + arr.length);

        // 数组是否包含指定对象，同时调用 == 和 equals方法
        var contained = ObjectUtils.containsElement(arr, "abc");
        System.out.println(contained);

        // 把Array转成Object数组
        Object[] objArr = ObjectUtils.toObjectArray(arr);
        System.out.println(objArr);

        // 对比2个对象是否相等， 同时调用 == 和 equals方法
        var equals = ObjectUtils.nullSafeEquals(objArr, arr);

        // 获取对象的hashCode，为null时返回0，为数组时，累加每个元素的hashCode
        var hashCode = ObjectUtils.nullSafeHashCode(arr);

//        ObjectUtils.caseInsensitiveValueOf()
        return "===========" + LocalDateTime.now();
    }
}
