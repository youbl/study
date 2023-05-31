package beinet.cn.googleauthenticatordemo.configs.loginValidator;

import beinet.cn.googleauthenticatordemo.configs.loginAction.TokenHelper;
import beinet.cn.googleauthenticatordemo.utils.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断Cookie是否存在有效登录信息的类
 *
 * @author youbl
 * @date 2023/1/4 18:18
 */
@Component
@Slf4j
public class CookieValidator implements Validator {
    private String loginUser;

    /**
     * 正常优先级
     *
     * @return 排序
     */
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getLoginName() {
        return loginUser;
    }

    @Override
    public boolean validated(HttpServletRequest request, HttpServletResponse response) {
        String cookName = TokenHelper.TOKEN_COOKIE_NAME;
        // 判断有没有cookie，有cookie时是否有效
        String token = ContextUtil.getCookie(cookName);
        log.info("url:{} token: {}", request.getRequestURI(), token);
        loginUser = TokenHelper.getLoginUserFromToken(token);
        if (!StringUtils.hasLength(loginUser)) {
            log.debug("token无效: {}", token);
            return false;
        }
        return true;
    }
}
