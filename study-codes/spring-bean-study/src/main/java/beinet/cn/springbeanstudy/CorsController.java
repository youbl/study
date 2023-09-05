package beinet.cn.springbeanstudy;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/8/31 16:47
 */
@RestController
public class CorsController {

    @GetMapping("cors/1")
    public String m1(HttpServletRequest request, HttpServletResponse response) {
        addAllowOrigin(request, response);

        // 如果前端带上了 withCredentials:true 后端要写cookie必须设置这个header
        response.addHeader("Access-Control-Allow-Credentials", "true");

        Cookie cookie = new Cookie("ck_n1", "abc");
        cookie.setPath("/");
        cookie.setMaxAge(86400); // MaxAge单位为秒
        response.addCookie(cookie);
        return "写入Cookie OK";
    }

    @GetMapping("cors/2")
    public String m2(HttpServletRequest request, HttpServletResponse response) {
        addAllowOrigin(request, response);

        // 如果前端带上了 withCredentials:true,则请求时会加上cookie，此时后端必须设置这个header
        response.addHeader("Access-Control-Allow-Credentials", "true");

        Cookie[] arr = request.getCookies();
        if (arr == null)
            return "没有Cookie";
        StringBuilder sb = new StringBuilder();
        for (Cookie item : arr) {
            if (sb.length() > 0)
                sb.append(";  ");
            sb.append(item.getName()).append(":").append(item.getValue());
        }
        return arr.length + "个cookie " + sb.toString();
    }

    private void addAllowOrigin(HttpServletRequest request, HttpServletResponse response) {
        // 调用 org.apache.catalina.connector.Request类方法
        String referer = request.getHeader("referer");
        if (!StringUtils.hasLength(referer)) {
            referer = getRequestDomain(request);
        }
        if (StringUtils.hasLength(referer)) {
            if (referer.endsWith("/"))
                referer = referer.substring(0, referer.length() - 1); // 注意不能有最后一个斜杠
            response.addHeader("Access-Control-Allow-Origin", referer);
        }
    }

    // 拼接协议+主机名+端口
    private static String getRequestDomain(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) {
            // Work around java.net.URL bug
            port = 80;
        }

        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80))
                || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }

        return url.toString();
    }
}
