package beinet.cn.demospringsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
            response.sendRedirect("/myLogin.html" + getReturnUrl(request));
        }
    }

    /**
     * 对于ajax请求，输出json响应
     *
     * @param response 响应上下文
     * @param msg      要输出的错误信息
     * @throws IOException 可能的异常
     */
    private static void ajaxResponse(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));

        Map<String, Object> data = new HashMap<>();
        data.put("msg", msg);
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 判断是否ajax请求
     *
     * @param request 当前请求上下文
     * @return 是否
     */
    private static boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("accept");
        if (header != null && header.contains("application/json"))
            return true;
        header = request.getHeader("x-requested-with");
        return header != null && header.equalsIgnoreCase("XMLHttpRequest");
    }

    /**
     * 获取当前请求的地址，作为登录页的返回url参数
     *
     * @param request 当前请求上下文
     * @return 当前请求地址
     * @throws UnsupportedEncodingException 可能的异常
     */
    private static String getReturnUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        String query = request.getQueryString();
        String rUrl = request.getRequestURI();
        if (query != null && !query.isEmpty())
            rUrl += "?" + query;

        return "?returnUrl=" + URLEncoder.encode(rUrl, "UTF-8");
    }
}
