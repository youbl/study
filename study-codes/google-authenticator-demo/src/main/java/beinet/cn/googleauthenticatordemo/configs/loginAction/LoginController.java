package beinet.cn.googleauthenticatordemo.configs.loginAction;

import beinet.cn.googleauthenticatordemo.utils.ContextUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/31 16:30
 */
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("login")
    public void doLogin(@RequestBody(required = false) LoginDto dto,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        dto = getUserPwd(dto, request);

        // 登录失败
        if (!loginService.processLogin(dto.getBeinetUser(), dto.getBeinetUser())) {
            addTokenCookie("", response);
            ContextUtil.endResponse(request, response, "账号或密码错误");
            return;
        }

        // 账号密码比对成功，写入Cookie
        String token = TokenHelper.buildToken(dto.getBeinetUser());
        addTokenCookie(token, response);

        if (!ContextUtil.isAjax(request)) {
            // 页面请求，进行302跳转
            String redirectUrl = request.getParameter("url");
            if (!StringUtils.hasLength(redirectUrl))
                redirectUrl = "index.html";
            ContextUtil.redirect(response, redirectUrl);
        } else {
            // ajax请求，返回json数据
            ContextUtil.writeJson(response, 0, dto.getBeinetUser() + " 登录成功", token);
        }
    }

    @GetMapping("logout")
    public void doLogout(HttpServletResponse response) {
        // 退出登录，注：这里没有清理两步验证的cookie，这样下次这台电脑登录可以不需要两步验证了
        // 你可以根据自己的业务需要，选择清理
        addTokenCookie("", response);
    }

    private LoginDto getUserPwd(LoginDto dto, HttpServletRequest request) {
        if (dto == null) {
            dto = new LoginDto();
        }
        if (!StringUtils.hasLength(dto.getBeinetUser())) {
            dto.setBeinetUser(request.getParameter("beinetUser"));
            dto.setBeinetPwd(request.getParameter("beinetPwd"));
        }
        return dto;
    }

    /**
     * 为响应添加登录cookie
     *
     * @param token    登录token，为空表示删除cookie
     * @param response 响应上下文
     */
    private void addTokenCookie(String token, HttpServletResponse response) {
        ContextUtil.addCookie(TokenHelper.TOKEN_COOKIE_NAME, token, response);
    }

    @Data
    public static class LoginDto {
        private String beinetUser;
        private String beinetPwd;
    }
}
