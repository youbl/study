package beinet.cn.lombokdemo.demos;

import lombok.val;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
public class ValDemo {
    public static String valDemo() {
        // val是lombom提供的语法糖，相当于 final int intVal = 123;
        val longVal1 = System.currentTimeMillis();
        val longVal2 = longVal1 + 123L;
        val str1 = "result: " + longVal2;

        // java10开始支持var关键字，已经不需要使用lombok的var了
        var str2 = str1 + "abc";
        return str2;
    }
}
