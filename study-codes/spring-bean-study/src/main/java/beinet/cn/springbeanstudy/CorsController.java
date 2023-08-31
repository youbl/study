package beinet.cn.springbeanstudy;

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
    private String corsSourceSite = "http://localhost:8808"; // 注意不能有最后一个斜杠

    @GetMapping("cors/1")
    public String m1(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", corsSourceSite);
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
        response.addHeader("Access-Control-Allow-Origin", corsSourceSite);
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
}
