package beinet.cn.googleauthenticatordemo.configs.loginValidator;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 登录信息类
 *
 * @author : youbl
 * @create: 2022/6/9 20:49
 */
@Data
public class AuthDetails {
    private String account;

    private String userAgent;

    public String getAccount() {
        if (!StringUtils.hasLength(account)) {
            return "匿名";
        }
        return account;
    }
}
