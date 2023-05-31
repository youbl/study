package beinet.cn.googleauthenticatordemo.configs;

import beinet.cn.googleauthenticatordemo.configs.loginValidator.AuthDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 这个类用于解析Controller的方法参数，添加一个当前登录用户的参数
 *
 * @author : youbl
 * @create: 2022/6/9 20:51
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 为Controller增加一个参数解析器
     *
     * @param resolvers 参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthDetailArgumentResolver());
    }

    /**
     * 自动解析Controller上的参数，并进行值设定的类
     */
    public class AuthDetailArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(AuthDetails.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            AuthDetails ret = new AuthDetails();

            // 调用者的信息
            ret.setUserAgent(webRequest.getHeader("user-agent"));

//        Principal principal = webRequest.getUserPrincipal();
//        if (principal == null) {
//            return ret;
//        }
//        ret.setAccount(principal.getName());
            // 在LoginFilter里，调用了 request.setAttribute("loginUser", item.getLoginName());
            ret.setAccount(webRequest.getAttribute("loginUser", 0) + "");

            return ret;
        }
    }

}
