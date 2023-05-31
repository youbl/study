package beinet.cn.googleauthenticatordemo.configs.loginAction;

import beinet.cn.googleauthenticatordemo.utils.ContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     *
     * @param request  请求上下文
     * @param response 响应上下文
     */
    public void processLogin(HttpServletRequest request, HttpServletResponse response, String username, String pwd) {
        log.debug("用户名: {}, 准备登录", username);
        if (!validateUser(username, pwd)) {
            log.debug("用户名: {}, 账号或密码错误", username);
            //throw new RuntimeException("账号或密码错误");
            addTokenCookie("", response);
            ContextUtil.endResponse(request, response, "账号或密码错误");
            return;
        }

        log.debug("用户名: {}, 账号密码认证成功", username);
        String token = TokenHelper.buildToken(username);
        addTokenCookie(token, response);

        String redirectUrl = request.getParameter("url");
        if (!StringUtils.hasLength(redirectUrl))
            redirectUrl = "index.html";

        if (!ContextUtil.isAjax(request)) {
            // 页面请求，进行302跳转
            ContextUtil.redirect(response, redirectUrl);
        } else {
            // ajax请求，返回json数据
            ContextUtil.writeJson(response, 0, username + " 登录成功", token);
        }
    }

    /**
     * 去数据库或ldap 验证用户名密码是否正确
     *
     * @param username 用户名
     * @param pwd      密码
     * @return 是否正确
     */
    public boolean validateUser(String username, String pwd) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(pwd)) {
            return false;
        }
        // todo: 这里写死账号密码，需要改成数据库读取和验证
        return username.equalsIgnoreCase("beinet") && pwd.equals("beinet");
    }

    /**
     * 为响应添加登录cookie
     *
     * @param token    登录token，为空表示删除cookie
     * @param response 响应上下文
     */
    public static void addTokenCookie(String token, HttpServletResponse response) {
        ContextUtil.addCookie(TokenHelper.TOKEN_COOKIE_NAME, token, response);
    }

}
