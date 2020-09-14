package beinet.cn.demospringsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 登录相关的配置，如：哪些地址要登录，角色是啥，登录地址、登录成功/失败操作等等
 */
@Configuration
public class BeinetAuthConfiguration extends WebSecurityConfigurerAdapter {
    /**
     * 根据用户名查找用户信息的Bean
     *
     * @return 用户服务类
     */
    @Bean
    UserDetailsService userService(PasswordEncoder encoder) {
        return new BeinetUserService(encoder);
    }

    /**
     * 不定义这个Bean，将会抛出异常：
     * There is no PasswordEncoder mapped for the id "null"
     *
     * @return 密码编码器
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BeinetPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/login")   // 对login的请求，忽略csrf，否则会导致无法登录，一直302
                .and()                              // 把前面的返回结果，转换回HttpSecurity，以便后续的流式操作
                .formLogin()                        // 开启form表单登录
                .loginPage("/myLogin.html")         // 使用自定义的登录表单
                .loginProcessingUrl("/login")       // 接收POST登录请求的处理地址
                .successHandler(new BeinetHandleSuccess()) // 登录验证通过后的处理器
                .failureHandler(new BeinetHandleFail())    // 登录验证失败后的处理器
                .permitAll()                        // 允许上述请求匿名访问, 注：不加这句，会导致302死循环
                .and()
                .authorizeRequests()                // 开始指定请求授权
                .antMatchers("/res/**").permitAll()     // res根路径及子目录请求，不限制访问
                .antMatchers("/news/**").hasRole("ROOT")// news根路径及子目录请求，要求ROOT角色才能访问
                .antMatchers("/time/**").hasRole("USER")// time根路径及子目录请求，要求USER角色才能访问
                .anyRequest().authenticated();      // 其它所有请求都要求登录后访问，但是不限制角色
    }

}
