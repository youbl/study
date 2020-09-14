package beinet.cn.demospringsecurity.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 登录用户信息类
 */
@Data
public class BeinetUser implements UserDetails {
    private String username = "";
    private String password = "";
    private List<String> roles = new ArrayList<>();


    /**
     * 添加用户角色
     *
     * @param role 角色
     */
    public void addRole(String role) {
        if (!StringUtils.isEmpty(role))
            roles.add(role);
    }

    /**
     * 返回所属角色列表
     *
     * @return 角色列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            for (String role : roles) {
                // Spring比较是按 ROLE_USER 形式进行比较
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        return authorities;
    }


    /**
     * 密码
     *
     * @return 密码
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 用户名
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否过期
     *
     * @return 是否
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否锁定
     *
     * @return 是否
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期，比如每月要求更换
     *
     * @return 是否
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用
     *
     * @return 是否
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
