package beinet.cn.googleauthenticatordemo.configs.loginAction;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
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
        String username;
        String pwd;
        if (dto == null || !StringUtils.hasLength(dto.getBeinetUser())) {
            username = request.getParameter("beinetUser");
            pwd = request.getParameter("beinetPwd");
        } else {
            username = dto.getBeinetUser();
            pwd = dto.getBeinetPwd();
        }
        loginService.processLogin(request, response, username, pwd);
    }

    @Data
    public static class LoginDto {
        private String beinetUser;
        private String beinetPwd;
    }
}
