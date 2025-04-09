package beinet.cn.springscheduledstudy.dynamicTrigger;

import lombok.Getter;
import lombok.Setter;

/**
 * 全局变量配置类
 * @author youbl
 * @since 2025/4/9 15:09
 */
public class DynamicVars {
    @Getter
    @Setter
    private static Long TimerMillis = 10000L;
}
