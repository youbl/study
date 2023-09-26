package beinet.cn.googleauthenticatordemo.configs.loginAction;

import beinet.cn.googleauthenticatordemo.authenticator.AuthenticatorService;
import beinet.cn.googleauthenticatordemo.configs.loginValidator.AuthDetails;
import beinet.cn.googleauthenticatordemo.utils.ContextUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 二次验证相关API
 *
 * @author youbl
 * @since 2023/5/31 19:19
 */
@RestController
@RequiredArgsConstructor
public class OtpController {
    private final AuthenticatorService authService;

    /**
     * 根据登录名，返回secureKey绑定状态，未绑定时进行绑定并返回二维码
     *
     * @param authDetails 登录信息
     * @return 绑定信息
     */
    @GetMapping("otp/status")
    public BindDto getBindStatus(AuthDetails authDetails) {
        String username = authDetails.getAccount();
        // 以前绑定过
        if (authService.existSecureKey(username)) {
            return new BindDto()
                    .setBinded(true)
                    .setUsername(username);
        }
        String authUrl = authService.generateAuthUrl(username);
        return new BindDto()
                .setBinded(false)
                .setUsername(username)
                .setQrCode(authUrl);
    }

    @PostMapping("otp/valid")
    public ResponseDto validOtpCode(@RequestParam int code,
                                    HttpServletResponse response,
                                    AuthDetails authDetails) {
        String username = authDetails.getAccount();
        if (authService.validateCode(username, code)) {
            // 用同样的规则生成otp的cookie
            String token = TokenHelper.buildToken(username);
            ContextUtil.addCookie(TokenHelper.OTP_COOKIE_NAME, token, response);
            return ResponseDto.ok();
        }
        return ResponseDto.fail("输入的验证码不匹配");
    }

    @Data
    @Accessors(chain = true)
    public static class BindDto {
        // 是否绑定了secureKey
        private boolean binded;
        // 未绑定时，生成并绑定到用户，然后返回给前端的二维码
        private String qrCode;
        // 当前登录用户
        private String username;
    }
}
