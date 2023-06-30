package beinet.cn.datadesensitizedemo.services;

import org.springframework.cglib.core.internal.Function;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/29 20:01
 */
public enum DesensitizeEnum {
    /**
     * 手机号，只保留前2位和后2位
     */
    PHONE(originStr -> originStr.replaceAll("^(\\d{2})\\d+(\\d{2})$", "$1**$2")),

    /**
     * 地址，只保留前3字和后3字
     */
    ADDRESS(originStr -> originStr.replaceAll("^(.{3}).+(.{3})$", "$1**$2")),

    /**
     * 密码，固定返回星号
     */
    PASSWORD(originStr -> "******"),

    /**
     * 姓名，调用静态方法
     */
    NAME(DesensitizeEnum::processName);


    /**
     * 脱敏用的方法
     */
    private Function<String, String> desensitizeMethod;

    // 构造函数
    DesensitizeEnum(Function<String, String> desensitizeMethod) {
        this.desensitizeMethod = desensitizeMethod;
    }

    /**
     * 返回当前枚举使用的脱敏方法
     *
     * @return method
     */
    public Function<String, String> getDesensitizeMethod() {
        return desensitizeMethod;
    }

    private static String processName(String name) {
        final String mask = "**";
        int len = name.length();
        if (len <= 1)
            return mask;
        if (len == 2)
            return name.substring(0, 1) + mask;
        return name.substring(0, 1) + mask + name.substring(len - 1, len);
    }
}
