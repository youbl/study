package beinet.cn.demospringsecurity.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个类用于登录失败后的处理。
 * 删除这个类，将使用默认操作, 302到登录页。
 */
public class BeinetHandleFail implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        BeinetAuthConfiguration.outputDenyMsg(response, exception.getMessage());
    }
}
