package beinet.cn.demospringsecurity.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户查找服务类，在 BeinetAuthConfiguration 里使用，用于用户查找
 */
@Service("beinetUserService")
public class BeinetUserService implements UserDetailsService {
    private PasswordEncoder encoder;

    /**
     * 带密码编码器的构造函数
     *
     * @param encoder 编码器
     */
    public BeinetUserService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * 根据用户名，查找用户信息返回。
     * 可以从数据库、内存或api远程查找
     *
     * @param username 用户名
     * @return 找到的用户信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("username can't be empty.");

        // 下面一般从数据库里查找用户
        if (username.equalsIgnoreCase("admin")) {
            BeinetUser ret = new BeinetUser();
            ret.setUsername("admin");
            // spring security内部会先进行编码，再比较，所以这里要编码后返回
            ret.setPassword(encoder.encode("beinet.cn"));
            ret.addRole("USER");
            ret.addRole("ROOT");
            return ret;
        }
        if (username.equalsIgnoreCase("user")) {
            BeinetUser ret = new BeinetUser();
            ret.setUsername("user");
            // spring security内部会先进行编码，再比较，所以这里要编码后返回
            ret.setPassword(encoder.encode("beinet"));
            ret.addRole("USER");
            return ret;
        }
        throw new UsernameNotFoundException(username + " not found.");
    }
}
