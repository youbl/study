package beinet.cn.demospringsecurity.security.argument;

import beinet.cn.demospringsecurity.security.BeinetUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AuthDetailArgumentResolver
 * AuthDetails参数注入解析器
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/20 11:58
 */
public class AuthDetailArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 判断参数类型，如果是 AuthDetails，则调用 resolveArgument方法生成该参数，并注入
     *
     * @param parameter 参数信息
     * @return true false
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AuthDetails.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthDetails ret = new AuthDetails();

        // 调用者的信息
        ret.setUserAgent(webRequest.getHeader("user-agent"));

        Principal principal = webRequest.getUserPrincipal();
        if (principal != null) {
            ret.setUserName(principal.getName());

            BeinetUser user = null;

            if (RememberMeAuthenticationToken.class.isInstance(principal)// 记住我用的token
                    && BeinetUser.class.isInstance(((RememberMeAuthenticationToken) principal).getPrincipal())) {
                user = (BeinetUser) ((RememberMeAuthenticationToken) principal).getPrincipal();
            } else if (UsernamePasswordAuthenticationToken.class.isInstance(principal) // 登录用的token
                    && BeinetUser.class.isInstance(((UsernamePasswordAuthenticationToken) principal).getPrincipal())) {
                user = (BeinetUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            }
            if (user != null) {
                List<String> roles = user.getRoles();
                if (roles != null)
                    ret.setRole(roles.stream().collect(Collectors.joining(",")));
            }
        }

        return ret;
    }
}
