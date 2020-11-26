package beinet.cn.demospringsecurity.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * BeinetAuthenticationFilter
 * 支持Basic方式登录，包括url登录和header登录
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/26 10:22
 */

public class BeinetAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 不加try，异常直接抛出
        if (!doLoginWithParameter(request)) {
            doLoginWithHeader(request);
        }
        filterChain.doFilter(request, response);
    }

    private boolean doLoginWithParameter(HttpServletRequest request) {
        String ak = request.getParameter("ak");
        String sk = request.getParameter("sk");
        if (!StringUtils.isEmpty(ak) && !StringUtils.isEmpty(sk)) {
            setToken(ak, sk, request);
            return true;
        }
        return false;
    }

    private boolean doLoginWithHeader(HttpServletRequest request) throws IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Basic ")) {
            return false;
        }
        String[] tokens = extractAndDecodeHeader(header);
        assert tokens.length == 2;

        String username = tokens[0];
        String password = tokens[1];
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            setToken(username, password, request);
            return true;
        }
        return false;
    }

    private void setToken(String user, String pwd, HttpServletRequest request) {
        BeinetUser authUser = new BeinetUser();
        authUser.setUsername(user);
        authUser.addRole("ROOT");

        // 第一个参数principal必须有用户名，第二个参数必须是对应这个用户名的密码
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser, pwd);
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private String[] extractAndDecodeHeader(String header) {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(':');
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}

