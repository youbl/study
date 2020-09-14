package beinet.cn.demospringsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 匿名用户访问需要权限的url会转到此入口，
 * 可以判断如果是ajax，返回json，否则返回302
 */
public class BeinetAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        if (isAjax(request)) {
            ajaxResponse(response, authException.getMessage());
        } else {
            response.sendRedirect("/myLogin.html");
        }
    }

    private static void ajaxResponse(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));

        Map<String, Object> data = new HashMap<>();
        data.put("msg", msg);
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
    }

    private static boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("accept");
        if (header != null && header.contains("application/json"))
            return true;
        header = request.getHeader("x-requested-with");
        return header != null && header.equalsIgnoreCase("XMLHttpRequest");
    }
}
