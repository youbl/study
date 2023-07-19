package beinet.cn.googleauthenticatordemo.configs.loginAction;

import beinet.cn.googleauthenticatordemo.utils.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/16 17:45
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    /**
     * 根据用户名密码，并进行登录
     */
    public boolean processLogin(String username, String pwd) {
        log.debug("用户名: {}, 准备登录", username);
        if (!validateUser(username, pwd)) {
            log.debug("用户名: {}, 账号或密码错误", username);
            //throw new RuntimeException("账号或密码错误");
            return false;
        }

        log.debug("用户名: {}, 账号密码认证成功", username);
        return true;
    }

    /**
     * 去数据库或ldap 验证用户名密码是否正确
     *
     * @param username 用户名
     * @param pwd      密码
     * @return 是否正确
     */
    private boolean validateUser(String username, String pwd) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(pwd)) {
            return false;
        }
        // todo: 这里写死账号密码，需要改成数据库读取和验证
        //return username.equalsIgnoreCase("beinet") && pwd.equals("beinet");

        // 去LDAP验证账号密码
        return validateFromLDAP(username, pwd);
    }

    /**
     * 使用账号密码去LDAP验证登录的方法
     * 这个方法，不需要在yml里配置账号密码
     *
     * @param username LDAP账号
     * @param pwd      LDAP密码
     * @return 成功与否
     */
    private boolean validateFromLDAP(String username, String pwd) {
        LdapTemplate ldapTemplate = SpringUtil.getBean(LdapTemplate.class);

        if (username.indexOf("@") <= 0)
            username += "@beinet.cn"; // 按ldap的uid规则拼接
        
        try {
            Object context = ldapTemplate.getContextSource().getContext(username, pwd);
            // log.info(context.toString());
            return true;
        } catch (Exception exp) {
            log.error(exp.getMessage());
            return false;
        }
    }

    /**
     * 使用账号密码去LDAP验证登录的方法之2
     * 这个方法，需要在yml里配置 spring.ldap.username 和 spring.ldap.password
     * 否则会报错：
     * Uncategorized exception occured during LDAP processing; nested exception is javax.naming.NamingException: [LDAP: error code 1 - 000004DC: LdapErr: DSID-0C090A5C, comment: In order to perform this operation a successful bind must be completed on the connection., data 0, v4563 ]; remaining name '/'
     *
     * @param username LDAP账号
     * @param pwd      LDAP密码
     * @return 成功失败
     */
    private boolean validateByLDAP2(String username, String pwd) {
        LdapTemplate ldapTemplate = SpringUtil.getBean(LdapTemplate.class);
        try {
            String filter = new EqualsFilter("samAccountName", username).toString();
            log.info(filter);
            // return ldapTemplate.authenticate("", "(uid=" + email + ")", pwd);
            return ldapTemplate.authenticate("", filter, pwd);
        } catch (Exception exp) {
            log.error(exp.getMessage());
            return false;
        }
    }

}
