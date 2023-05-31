package beinet.cn.googleauthenticatordemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/31 16:04
 */
@Slf4j
public final class ContextUtil {

    // 登录输入页地址
    public static final String loginPage = "/login.html";

    /**
     * 终止响应，并返回错误信息
     *
     * @param request  请求上下文
     * @param response 响应上下文
     * @param msg      错误信息
     */
    public static void endResponse(HttpServletRequest request, HttpServletResponse response, String msg) {
        if (!isAjax(request)) {
            String originUrl = request.getRequestURI();
            String redirectUrl = loginPage + "?url=" + URLEncoder.encode(originUrl, StandardCharsets.UTF_8);
            redirect(response, redirectUrl);
        } else {
            writeJson(response, 500, msg, null);
        }
    }

    public static void writeJson(HttpServletResponse response, int code, String msg, Object obj) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("code", code);
            data.put("msg", msg);
            if (obj != null) {
                data.put("data", obj);
            }
            String json = SpringUtil.getBean(ObjectMapper.class).writeValueAsString(data);
            response.setContentType("application/json; charset=UTF-8");
            response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exp) {
            log.error("登录出错", exp);
        }
    }


    /**
     * 重定向到index首页
     *
     * @param response 响应上下文,一般是 ContentCachingResponseWrapper
     * @param url      跳转地址
     */
    public static void redirect(HttpServletResponse response, String url) {
        if (!StringUtils.hasLength(url)) {
            url = "/index.html";
        } else if (!url.startsWith("/")) {
            url = "/" + url;
        }
        response.setStatus(302);
        String prefix = getPrefix();
        if (!url.startsWith(prefix))
            url = prefix + url;
        response.setHeader("Location", url);//设置新请求的URL
    }


    /**
     * 判断是否ajax请求
     *
     * @param request 当前请求上下文
     * @return 是否
     */
    public static boolean isAjax(HttpServletRequest request) {
        // 先判断这个，提升效率
        String header = request.getHeader("x-requested-with");
        if (header != null && header.equalsIgnoreCase("XMLHttpRequest"))
            return true;

        header = request.getHeader("accept");
        if (header != null && header.contains("application/json"))
            return true;
        // x-requested-with: XMLHttpRequest
        return false;
    }


    /**
     * 为响应添加7天有效期的cookie
     *
     * @param name     cookie名
     * @param value    cookie值，为空表示删除cookie
     * @param response 响应上下文
     */
    public static void addCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");

        if (StringUtils.hasLength(value)) {
            cookie.setMaxAge(7 * 24 * 3600);
            cookie.setValue(value);
        } else {
            cookie.setMaxAge(0);
        }
        response.addCookie(cookie);
    }


    public static String getCookie(String name) {
        if (!StringUtils.hasLength(name))
            return "";

        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
            return "";
        for (Cookie cookie : cookies) {
            if (name.equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }


    private static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes == null ? null : servletRequestAttributes.getRequest();
    }

    private static String getPrefix() {
        String prefix = SpringUtil.getBean(Environment.class).getProperty("server.servlet.context-path");
        return prefix == null ? "" : prefix;
    }
}
