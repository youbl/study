package beinet.cn.demospringsecurity.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 已登录用户访问无权限的url时，会跳到这里
 */
public class BeinetHandleAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        BeinetAuthConfiguration.outputDenyMsg(response, accessDeniedException.getMessage());
    }
}