package beinet.cn.lombokdemo.demos;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
@AllArgsConstructor
public class NonNullDemo {
    // 注解在类的字段上，必须配合AllArgsConstructor或RequiredArgsConstructor使用，自己写的构造函数无效
    @NonNull
    private String field;

    public String getField() {
        return field;
    }

    // 传入的arg参数不能为null
    public static String nonNullDemo2(@NonNull Integer arg) {
        return arg.toString();
    }
}
/*
反编译的结果：
public class NonNullDemo {
    private String field;

    public NonNullDemo(String field) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

    public static String nonNullDemo2(@NonNull Integer arg) {
        if (arg != null) {
            return arg.toString();
        }
        throw new NullPointerException("arg is marked non-null but is null");
    }
}
* * */