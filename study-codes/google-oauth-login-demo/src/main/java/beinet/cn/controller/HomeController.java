package beinet.cn.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Collections;
import java.util.Enumeration;

@RestController
@Slf4j
public class HomeController {

    private static final String CLIENT_ID = "CLIENT_ID.apps.googleusercontent.com";

    @GetMapping(value = "/", produces = "text/plain")
    public String index(HttpServletRequest request) {
        String ret = getRequestData(request);
        log.warn(ret);
        return ret;
    }

    /**
     * 对应前端login1.html的回调地址
     * @param request 请求上下文
     * @return 完整用户信息
     */
    @PostMapping(value = "/", produces = "text/plain")
    public GoogleIdToken.Payload indexPost(HttpServletRequest request) {
        // 读取谷歌响应的数据并打印，响应数据参考：
        /*
POST http://localhost:8801/ttt/

IP-addr:0:0:0:0:0:0:0:1 Port:14663

All Headers:
host : localhost:8801
connection : keep-alive
content-length : 1221
cache-control : max-age=0
upgrade-insecure-requests : 1
origin : http://localhost:8801
content-type : application/x-www-form-urlencoded
user-agent : Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36
referer : http://localhost:8801/ttt/login1.html
accept-encoding : gzip, deflate, br, zstd
accept-language : zh-CN,zh;q=0.9
cookie : g_state={"i_l":0}; g_csrf_token=22c220a0407a7696
Body:
credential=aa.bb.cc-dd-ee-ff&g_csrf_token=22c220a0407a7696
        * */
//        String ret = getRequestData(request);
//        log.warn(ret);
        csrfTokenValid(request);
        return getMailFromCredential(request);
    }

    /**
     * 对应前端login2.html的校验接口
     * @param dto google返回的token数据
     * @return 完整用户信息
     */
    @PostMapping(value = "/valid")
    public GoogleIdToken.Payload validToken(@RequestBody ValidDto dto) {
        return getMailFromCredential(dto.getCredential());
    }

    private String getRequestData(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURL())
                .append("\r\n\n");
        sb.append("IP-addr:")
                .append(request.getRemoteAddr())
                .append(" Port:")
                .append(request.getRemotePort())
                .append("\r\n\nAll Headers:\r\n");

        Enumeration<String> allHeader = request.getHeaderNames();
        while (allHeader.hasMoreElements()) {
            String header = allHeader.nextElement();
            Enumeration<String> vals = request.getHeaders(header);
            while (vals.hasMoreElements()) {
                String val = vals.nextElement();
                sb.append(header)
                        .append(" : ")
                        .append(val)
                        .append("\r\n");
            }
        }
        sb.append("Body:\r\n")
                .append(getBodyStr(request));
        String ret = sb.toString();
        return ret;
    }

    @SneakyThrows
    private String getBodyStr(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        // credential=xxx.xxx.xxx-xxx-xxx-xxx&g_csrf_token=22c220a0407a7696
        return requestBody.toString();
    }

    /**
     * 参考这里验证令牌：<a href="https://developers.google.com/identity/gsi/web/guides/verify-google-id-token?hl=zh-cn">...</a>
     * Cookie里的g_csrf_token必须等于body里的g_csrf_token
     *
     * @param request 请求上下文
     */
    private void csrfTokenValid(HttpServletRequest request) {
        String name = "g_csrf_token";
        String cookieCsrfToken = getCookie(request, name);
        if (!StringUtils.hasLength(cookieCsrfToken)) {
            throw new RuntimeException("Google csrf token is empty");
        }
        String bodyCsrfToken = getBody(request, name);
        if (!cookieCsrfToken.equals(bodyCsrfToken)) {
            throw new RuntimeException("Google csrf token verify failed");
        }
    }

    /**
     * 从请求body里获取google的jwt，并进行校验，再返回完整用户信息
     * @param request 请求上下文
     * @return 用户信息
     */
    @SneakyThrows
    private GoogleIdToken.Payload getMailFromCredential(HttpServletRequest request) {
        String name = "credential";
        String credential = getBody(request, name);
        Assert.isTrue(StringUtils.hasLength(credential), "Google credential is empty");
        return getMailFromCredential(credential);
    }

    /**
     * 从请求body里获取google的jwt，并进行校验，再返回完整用户信息
     * @param credential 请求上下文
     * @return 用户信息
     */
    @SneakyThrows
    private GoogleIdToken.Payload getMailFromCredential(String credential) {
        // 官方文档没写Builder的2个参数怎么来的，参考这里写的：https://stackoverflow.com/questions/37172082/android-what-is-transport-and-jsonfactory-in-googleidtokenverifier-builder
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        GoogleIdToken idToken = verifier.verify(credential);
        Assert.notNull(idToken, "Google credential is not valid");
        GoogleIdToken.Payload payload = idToken.getPayload();
        log.info("name：{}", payload.get("name"));
        log.info("family_name：{}", payload.get("family_name"));
        log.info("given_name：{}", payload.get("given_name"));
        log.info("picture：{}", payload.get("picture"));
        return payload;
    }

    private String getCookie(HttpServletRequest request, String name) {
        Assert.isTrue(StringUtils.hasLength(name), "name不能为空");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private String getBody(HttpServletRequest request, String name) {
        Assert.isTrue(StringUtils.hasLength(name), "name不能为空");
        String ret = request.getParameter(name);
        return ret == null ? "" : ret;
    }

    @Data
    public static class ValidDto {
        private String credential;
    }
}
