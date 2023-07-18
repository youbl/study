package beinet.cn.googleauthenticatordemo.configs.loginAction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/16 17:45
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    /**
     * 根据用户名密码，并进行登录
     */
    public boolean processLogin(String username, String pwd) {
        log.debug("用户名: {}, 准备登录", username);
        if (!validateUser(username, pwd)) {
            log.debug("用户名: {}, 账号或密码错误", username);
            //throw new RuntimeException("账号或密码错误");
            return false;
        }

        log.debug("用户名: {}, 账号密码认证成功", username);
        return true;
    }

    /**
     * 去数据库或ldap 验证用户名密码是否正确
     *
     * @param username 用户名
     * @param pwd      密码
     * @return 是否正确
     */
    private boolean validateUser(String username, String pwd) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(pwd)) {
            return false;
        }
        // todo: 这里写死账号密码，需要改成数据库读取和验证
        return username.equalsIgnoreCase("beinet") && pwd.equals("beinet");
    }


}
