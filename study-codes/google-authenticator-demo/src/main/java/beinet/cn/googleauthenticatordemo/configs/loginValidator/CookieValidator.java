package beinet.cn.googleauthenticatordemo.configs.loginValidator;

import beinet.cn.googleauthenticatordemo.configs.loginAction.TokenHelper;
import beinet.cn.googleauthenticatordemo.utils.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // 判断有没有cookie，有cookie时是否有效
        loginUser = getUsername(TokenHelper.TOKEN_COOKIE_NAME, request);
        if (!StringUtils.hasLength(loginUser)) {
            // log.debug("token无效: {}", token);
            return false;
        }
        // 不需要二次验证
        if (noNeedOTPCodeValid(request)) {
            return true;
        }
        return checkOTPStatus(request, loginUser);
    }


    // 当前请求是否需要进行二次验证
    private boolean noNeedOTPCodeValid(HttpServletRequest request) {
        // 无须二次验证的url正则，otp相关接口用于验证otpcode，所以只要登录就可以了
        final Pattern patternRequest = Pattern.compile("(?i)^/otp/");
        String url = request.getServletPath();
        Matcher matcher = patternRequest.matcher(url);
        return matcher.find();

    }

    // 检查请求上下文里的二次验证cookie状态
    private boolean checkOTPStatus(HttpServletRequest request, String loginUser) {
        String otpUser = getUsername(TokenHelper.OTP_COOKIE_NAME, request);
        if (!StringUtils.hasLength(otpUser)) {
            // log.debug("otp cookie token无效: {}", token);
            return false;
        }
        // otp的用户名必须等于登录用户名
        return otpUser.equals(loginUser);
    }

    private String getUsername(String cookieName, HttpServletRequest request) {
        String token = ContextUtil.getCookie(cookieName);
        log.info("url:{} {}: {}", request.getRequestURI(), cookieName, token);
        return TokenHelper.getLoginUserFromToken(token);
    }
}
