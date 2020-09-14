package beinet.cn.demospringsecurity.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义的密码编码器，直接明文比较处理
 */
public class BeinetPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return String.valueOf(rawPassword).equals(encodedPassword);
    }
}
