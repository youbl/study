package beinet.cn.demospringsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个类用于登录失败后的处理。
 * 删除这个类，将使用默认操作, 302到登录页。
 */
public class BeinetHandleFail implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));

        Map<String, Object> data = new HashMap<>();
        data.put("msg", exception.getMessage());
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes("UTF-8"));
    }
}
