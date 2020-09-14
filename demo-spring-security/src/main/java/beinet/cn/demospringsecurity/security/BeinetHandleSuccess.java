package beinet.cn.demospringsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个类用于完成登录成功后的处理。
 * 删除这个类，将使用默认操作，302到登录前的url。
 */
public class BeinetHandleSuccess implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // response.sendRedirect("/user/sub");
        response.setStatus(HttpStatus.OK.value());

        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));
        response.setContentType("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("msg", authentication.getName() + " 登录成功");
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes("UTF-8"));
    }
}