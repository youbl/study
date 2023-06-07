package beinet.cn.lombokdemo.demos;

import lombok.SneakyThrows;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 13:45
 */
public class SneakyThrowsDemo {
    // SneakyThrows用处：方法里抛出异常时，把它包装成RuntimeException再抛出
    // 这样避免手写try catch，也避免编译时异常（即在方法签名后写 throw Exception）
    @SneakyThrows
    public int test1() {
        throw new Exception("abc");
    }

    // 下面2个方法，等效于上面的方法
    public int test2() throws Exception {
        throw new Exception("abc");
    }

    public int test3() {
        try {
            throw new Exception("abc");
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
