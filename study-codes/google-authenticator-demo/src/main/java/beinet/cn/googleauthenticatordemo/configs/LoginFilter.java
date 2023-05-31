package beinet.cn.googleauthenticatordemo.configs;

import beinet.cn.googleauthenticatordemo.configs.loginAction.LoginService;
import beinet.cn.googleauthenticatordemo.configs.loginValidator.Validator;
import beinet.cn.googleauthenticatordemo.utils.ContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 这个类用于拦截所有的请求，并验证是否登录
 *
 * @author youbl
 * @since 2023/5/31 14:28
 */
@Component
@RequiredArgsConstructor
public class LoginFilter extends OncePerRequestFilter {
    private final LoginService loginService;
    private final List<Validator> validatorList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request.getRequestURL() 带有域名，所以不用
        //request.getRequestURI() 带有ContextPath，所以不用
        //String url = request.getServletPath();
        //log.info("收到请求: {}", url);

        // 逐一调用验证器，比如不需要登录的页面、比如SDK登录等等
        for (Validator item : validatorList) {
            if (item.validated(request, response)) {
                // 添加登录后的信息
                request.setAttribute("loginUser", item.getLoginName());

                filterChain.doFilter(request, response);
                return;
            }
        }

        ContextUtil.endResponse(request, response, "请重新登录");
    }

}
