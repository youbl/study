package beinet.cn.googleauthenticatordemo.configs.loginValidator;

import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录认证接口
 *
 * @author youbl
 * @date 2023/1/4 18:17
 */
public interface Validator extends Ordered {
    /**
     * 获取登录名
     *
     * @return 登录账号名
     */
    String getLoginName();

    /**
     * 是否认证通过
     *
     * @param request  请求上下文
     * @param response 响应上下文
     * @return 是否认证通过
     */
    boolean validated(HttpServletRequest request, HttpServletResponse response);
}
