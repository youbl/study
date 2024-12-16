package cn.beinet.core.web.context;

import cn.beinet.core.base.configs.ConfigConst;
import cn.beinet.core.utils.IpHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static cn.beinet.core.web.context.ContextConstants.HEADER_API_ID;
import static cn.beinet.core.web.context.ContextConstants.HEADER_APPLICATION;
import static cn.beinet.core.web.context.ContextConstants.HEADER_IS_BOSS;
import static cn.beinet.core.web.context.ContextConstants.HEADER_IS_DEVOPS;
import static cn.beinet.core.web.context.ContextConstants.HEADER_LANGUAGE;
import static cn.beinet.core.web.context.ContextConstants.HEADER_MACHINE_ID;
import static cn.beinet.core.web.context.ContextConstants.HEADER_MACHINE_NAME;
import static cn.beinet.core.web.context.ContextConstants.HEADER_MEMBER_ID;
import static cn.beinet.core.web.context.ContextConstants.HEADER_MEMBER_NICKNAME;
import static cn.beinet.core.web.context.ContextConstants.HEADER_MEMBER_ROLE;
import static cn.beinet.core.web.context.ContextConstants.HEADER_OS;
import static cn.beinet.core.web.context.ContextConstants.HEADER_PREFIX;
import static cn.beinet.core.web.context.ContextConstants.HEADER_PREFIX_ACCEPT;
import static cn.beinet.core.web.context.ContextConstants.HEADER_PRODUCT;
import static cn.beinet.core.web.context.ContextConstants.HEADER_REQUEST_TIME;
import static cn.beinet.core.web.context.ContextConstants.HEADER_SOURCE;
import static cn.beinet.core.web.context.ContextConstants.HEADER_STARTUP_TIME;
import static cn.beinet.core.web.context.ContextConstants.HEADER_TENANT_ID;
import static cn.beinet.core.web.context.ContextConstants.HEADER_USER_EMAIL;
import static cn.beinet.core.web.context.ContextConstants.HEADER_USER_ID;
import static cn.beinet.core.web.context.ContextConstants.HEADER_VERSION;
import static cn.beinet.core.web.context.ContextConstants.HEADER_X_TRACE_ID;


@Slf4j
public class ContextUtils {

    public static final String DEFAULT_PRODUCT = "beinet";

    public static String getApiId() {
        return getHeader(HEADER_API_ID);
    }

    public static Long getMemberId() {
        return getLongHeader(HEADER_MEMBER_ID);
    }

    public static String getMemberRole() {
        return getHeader(HEADER_MEMBER_ROLE);
    }

    public static String getMemberNickname() {
        return getHeader(HEADER_MEMBER_NICKNAME);
    }

    public static void setMemberId(Long memberId) {
        setAttribute(HEADER_MEMBER_ID, memberId.toString());
    }

    public static Long getUserId() {
        return getLongHeader(HEADER_USER_ID);
    }

    public static String getProduct() {
        String product = getHeader(HEADER_PRODUCT);
        return StringUtils.hasLength(product) ? product : DEFAULT_PRODUCT;
    }

    public static void setProduct(String product) {
        setAttribute(HEADER_PRODUCT, product);
    }

    public static void setUserId(Long userId) {
        MDC.put("userId", userId.toString());
        setAttribute(HEADER_USER_ID, userId.toString());
    }

    public static String getUserEmail() {
        return getHeader(HEADER_USER_EMAIL);
    }

    public static void setUserEmail(String userEmail) {
        setAttribute(HEADER_USER_EMAIL, userEmail);
    }

    public static Long getTenantId() {
        return getLongHeader(HEADER_TENANT_ID);
    }

    public static void setIsBoss(Long isBoss) {
        setAttribute(HEADER_IS_BOSS, isBoss.toString());
    }

    public static void setTenantId(Long tenantId) {
        setAttribute(HEADER_TENANT_ID, tenantId.toString());
    }

    public static Boolean getIsBoss() {
        String isBoss = getHeader(HEADER_IS_BOSS);
        return "0".equals(isBoss);
    }

    public static Boolean getIsDevops() {
        String isDevops = getHeader(HEADER_IS_DEVOPS);
        return "true".equals(isDevops);
    }

    public static String getTraceId() {
        return getHeader(HEADER_X_TRACE_ID);
    }

    public static void setTraceId(String traceId) {
        MDC.put("traceId", traceId);
        setAttribute(HEADER_X_TRACE_ID, traceId);
    }

    public static String getMachineId() {
        return getHeader(HEADER_MACHINE_ID);
    }

    public static String getMachineName() {
        return getHeader(HEADER_MACHINE_NAME);
    }

    public static String getVersion() {
        return getHeader(HEADER_VERSION);
    }

    public static String getLanguage() {
        var et = getHeader(HEADER_LANGUAGE);
        return StringUtils.hasLength(et) ? et : "en-US";
    }

    public static String getOS() {
        return getHeader(HEADER_OS);
    }

    public static String getSource() {
        String source = getHeader(HEADER_SOURCE);
        return StringUtils.hasLength(source) ? source : getOS();
    }

    public static String getStartupTime() {
        return getHeader(HEADER_STARTUP_TIME);
    }

    /**
     * 按优先级顺序获取用户IP
     *
     * @return 单个IP
     */
    public static String getIp() {
        try {
            HttpServletRequest request = getRequest();
            return request != null ? IpHelper.getIpAddr(request) : null;
        } catch (Exception exp) {
            log.error("取IP出错", exp);
            return "";
        }
    }

    /**
     * 返回完整用户IP，包括所有header
     *
     * @return 所有IP
     */
    public static String getFullIp() {
        try {
            HttpServletRequest request = getRequest();
            return request != null ? IpHelper.getClientIp(request) : null;
        } catch (Exception exp) {
            log.error("getFullIpErr", exp);
            return "";
        }
    }

    /**
     * 获取当前请求url
     *
     * @param getMethod  是否拼接METHOD
     * @param getReferer 是否拼接Referer
     * @return 请求url
     */
    public static String getRequestUrl(boolean getMethod, boolean getReferer) {
        try {
            HttpServletRequest request = getRequest();
            if (request != null) {
                String para = request.getQueryString();
                if (StringUtils.hasLength(para))
                    para = "?" + para;
                else
                    para = "";

                String method = getMethod ? request.getMethod() + " " : "";
                String referer = getReferer ? " refer:" + request.getHeader("referer") : "";
                return method + request.getRequestURL() + para + referer;
            }
        } catch (Exception exp) {
            log.error("取url出错", exp);
        }
        return "";
    }

    /**
     * 设置当前时间戳到header中
     */
    public static void setRequestTime() {
        long timestamp = System.currentTimeMillis();
        setAttribute(HEADER_REQUEST_TIME, String.valueOf(timestamp));
    }

    /**
     * 获取header里的请求时间戳
     *
     * @return 请求时间戳
     */
    public static long getRequestTime() {
        Long result = getLongHeader(HEADER_REQUEST_TIME);
        return result == null ? 0 : result;
    }


    /**
     * 透传头部信息
     *
     * @return 获取需要透传的header
     */
    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        try {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                // 透传x-开头的所有头部信息
                HttpServletRequest request = servletRequestAttributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames != null && headerNames.hasMoreElements()) {
                    try {
                        String key = headerNames.nextElement();
                        if (isTransfer(key)) {
                            headers.put(key, request.getHeader(key));
                        }
                    } catch (Exception e) {
                        // headerNames.nextElement() 可能出现空指针
                        log.warn("获取header某个key异常", e);
                    }
                }
                try {
                    // 提取attributes
                    String[] attributeNames =
                            servletRequestAttributes.getAttributeNames(ServletRequestAttributes.SCOPE_REQUEST);
                    for (String name : attributeNames) {
                        if (StringUtils.hasLength(name) && !headers.containsKey(name) && isTransfer(name)) {
                            Object val =
                                    servletRequestAttributes.getAttribute(name, ServletRequestAttributes.SCOPE_REQUEST);
                            headers.put(name, (String) val);
                        }
                    }
                } catch (IllegalStateException illExp) {
                    // Async执行feign时，servletRequestAttributes.getAttributeNames会抛异常
                    log.error("取header.getAttributeNames异常", illExp);
                }
            }
        } catch (Exception exp) {
            log.error("取header异常:", exp);
        }
        // 如果没有traceId自动生成
        if (!headers.containsKey(HEADER_X_TRACE_ID)) {
            headers.put(HEADER_X_TRACE_ID, UUID.randomUUID().toString());
        }
        // 设置当前服务名
        headers.put(HEADER_APPLICATION, ConfigConst.getAppName());
        return headers;
    }

    public static Map<String, String> getAllHeaders() {
        Map<String, String> ret = new HashMap<>();
        try {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                ret.put("", request.getMethod() + " " + request.getRequestURL());
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames != null && headerNames.hasMoreElements()) {
                    String key = headerNames.nextElement();

                    StringBuilder allVal = new StringBuilder();
                    Enumeration<String> headerVals = request.getHeaders(key);
                    while (headerVals != null && headerVals.hasMoreElements()) {
                        if (!allVal.isEmpty())
                            allVal.append(",");
                        allVal.append(headerVals.nextElement());
                    }
                    ret.put(key, allVal.toString());
                }
            }
        } catch (Exception exp) {
            log.error("getAllHeadersErr", exp);
        }
        return ret;
    }

    /**
     * 判断key是否透传
     *
     * @param key 键
     * @return 是否透传
     */
    public static boolean isTransfer(String key) {
        return key != null &&
                (key.startsWith(HEADER_PREFIX) ||
                        key.startsWith(HEADER_PREFIX_ACCEPT) ||
                        key.equalsIgnoreCase(HEADER_X_TRACE_ID) ||
                        key.equalsIgnoreCase(HEADER_PRODUCT));
    }

    public static String getCookie(String name) {
        if (!StringUtils.hasLength(name))
            return "";

        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return "";
        for (Cookie cookie : cookies) {
            if (name.equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public static void clearHeaders() {
        RequestContextHolder.resetRequestAttributes();
    }

    /**
     * 先从请求上下文，获取 request.setAttribute 设置的属性；
     * 不存在时，从请求的headers中获取
     * @param key 属性key
     * @return 属性在上下文里的值
     */
    public static String getHeader(String key) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        try {
            String value = (String) servletRequestAttributes.getAttribute(key, ServletRequestAttributes.SCOPE_REQUEST);
            if (value == null) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                value = request.getHeader(key);
            }
            return value;
        } catch (IllegalStateException illExp) {
            // Async执行时，servletRequestAttributes.getAttribute会抛异常
            // j.l.IllegalStateException: Cannot ask for request attribute - request is not active anymore!
            return "";
        }
//        HttpServletRequest request = servletRequestAttributes.getRequest();
//        String value = request.getHeader(key);
//        if (value == null) {
//            value = (String) servletRequestAttributes.getAttribute(key, ServletRequestAttributes.SCOPE_REQUEST);
//        }
    }

    public static Long getLongHeader(String key) {
        String value = getHeader(key);
        if (value != null) {
            return Long.valueOf(value);
        }
        return null;
    }

    public static void setAttribute(String key, String value) {
        if (ConfigConst.isJobApp()) {
            throw new RuntimeException("Job不允许设置上下文，请检查代码，并修改实现方案");
        }
        init(null).setAttribute(key, value, ServletRequestAttributes.SCOPE_REQUEST);
    }

    public static RequestAttributes reset(Map<String, String> initialise) {
        RequestContextHolder.setRequestAttributes(null);
        return init(initialise);
    }

    public static RequestAttributes init(Map<String, String> initialise) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            return attributes;
        }
        attributes = new ServletWebRequest(new CustomRequest(new HashMap<>()));
        RequestContextHolder.setRequestAttributes(attributes);
        if (Objects.isNull(initialise)) {
            return attributes;
        }
        // 复制初始值
        for (var key : initialise.keySet()) {
            attributes.setAttribute(key, initialise.get(key), ServletRequestAttributes.SCOPE_REQUEST);
        }
        return attributes;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes == null ? null : servletRequestAttributes.getRequest();
    }
}
