package beinet.cn.reflectionstudy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ThreadLocalTest {
    @Test
    public void addThreadLocalAndGC() {
        outputMaps();
        int oldLen = getThreadLocalMapEntryLen();

        new ThreadLocal<Dto>().set(new Dto(123, "abc"));
        int newLen = getThreadLocalMapEntryLen();
        Assert.isTrue(oldLen + 1 == newLen, "应该多了一项啊");
        outputMaps();

        System.gc();

        //  Assert.isTrue(oldLen == getThreadLocalMapEntryLen(), "为啥没有被GC？？");
        outputMaps();
        // 最后一个outputMaps输出的结果，一定有一项如下：
        // key是 null    value是 ThreadLocalTest.Dto(id=123, name=abc)
        // 因为key被gc回收了，value还在ThreadLocalMap的数组里，如果Thread一直没有结束，可能导致内存泄露

        new ThreadLocal<Dto>().set(new Dto(456, "abc"));
        outputMaps();
        System.gc();
        outputMaps();

        // 在继续调用ThreadLocalMap的set()、get() 或 remove()方法时，会清理这些key为null的数据
        // 需要注意的是 set 后调用的是 cleanSomeSlots方法，清理部分数据
        new ThreadLocal<Dto>().set(new Dto(789, "abc"));
        System.gc();
        outputMaps();
    }

    void outputMaps() {
        Object map = getThreadLocalMap();
        Object arr = getThreadLocalMapEntryArray(map);
        outputArr(arr);
    }

    // 获取当前线程的ThreadLocal清单
    Object getThreadLocalMap() {
        Thread thread = Thread.currentThread();
        return getField(thread, "threadLocals");
    }

    Object getThreadLocalMapEntryArray(Object map) {
        return getField(map, "table");
    }

    Object getField(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception exp) {
            System.out.printf("出错了:" + exp.getMessage());
            return null;
        }
    }

    int getThreadLocalMapEntryLen() {
        Object map = getThreadLocalMap();
        Object entryArr = getThreadLocalMapEntryArray(map);

        int ret = 0;
        int len = Array.getLength(entryArr);
        for (int i = 0, j = len; i < j; i++) {
            Object item = Array.get(entryArr, i);
            if (item == null)
                continue;
            ret++;
        }
        return ret;
    }

    void outputArr(Object arr) {
        int realLen = 0;
        int len = Array.getLength(arr);
        for (int i = 0, j = len; i < j; i++) {
            Object item = Array.get(arr, i);
            if (item == null)
                continue;
            outputThreadLocalEntry(item);
            realLen++;
        }
        System.out.println("    共" + realLen + "项");
    }

    void outputThreadLocalEntry(Object entry) {
        Object key = null;
        // entry.get() 是 WeakRefrence定义的方法
        if (entry instanceof WeakReference) {
            key = ((WeakReference) entry).get();
        }
        System.out.println("key是 " + key + "    value是 " + getField(entry, "value"));
    }

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
