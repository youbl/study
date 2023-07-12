package beinet.cn.frontstudy.testEnumList;

import org.springframework.context.annotation.Description;

@Description("这是季度枚举")
public enum Test234Enum {
    SUMMER("我是大夏天", 789),
    SPRING("我是暖春天", 345),
    WINTER("我是冷冬天", 1212);

    private String str;
    private int iii;

    Test234Enum(String str, int iii) {
        this.str = str;
        this.iii = iii;
    }

    public String getStr() {
        return str;
    }
}
