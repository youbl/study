package beinet.cn.frontstudy.configTest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 新类
 *
 * @author youbl
 * @date 2023/3/16 11:07
 */
@Component
public class ConfigTest1 {

    @Value("${beinet.newVal:}")
    String str3;

    public String getStr3() {
        return str3;
    }
}
