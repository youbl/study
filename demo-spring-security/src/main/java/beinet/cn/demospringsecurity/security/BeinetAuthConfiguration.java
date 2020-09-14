package beinet.cn.demospringsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 登录相关的配置，如：哪些地址要登录，角色是啥，登录地址、登录成功/失败操作等等
 */
@Configuration
public class BeinetAuthConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/login")   // 对login的请求，忽略csrf，否则会导致无法登录，一直302
                .and()                              // 把前面的返回结果，转换回HttpSecurity，以便后续的流式操作
                .formLogin()                        // 开启form表单登录
                .loginPage("/myLogin.html")         // 使用自定义的登录表单
                .loginProcessingUrl("/login")       // 接收POST登录请求的处理地址
                .permitAll()                        // 允许上述请求匿名访问, 注：不加这句，会导致302死循环
                .and()
                .authorizeRequests()                // 开始指定请求授权
                .antMatchers("/res/**").permitAll()     // res根路径及子目录请求，不限制访问
                .antMatchers("/news/**").hasRole("ROOT")// news根路径及子目录请求，要求ROOT角色才能访问
                .antMatchers("/time/**").hasRole("USER")// time根路径及子目录请求，要求USER角色才能访问
                .anyRequest().authenticated();      // 其它所有请求都要求登录后访问，但是不限制角色
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 也可以使用 org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder，性能慢一点
        BeinetPasswordEncoder encoder = new BeinetPasswordEncoder();

        auth.inMemoryAuthentication()           // 使用内存用户校验
                .passwordEncoder(encoder)       // 密码编码器，用户提交的密码会用它编码后，再与原始密码比较
                .withUser("admin")                          // 添加一个用户，命名为admin
                .password(encoder.encode("beinet.cn"))   // admin用户的密码
                .roles("ROOT", "USER")                                // admin用户的角色有2个，既是ROOT也是USER
                .and()
                .withUser("user")                           // 添加第二个用户，命名 user
                .password(encoder.encode("beinet"))      // user用户的密码
                .roles("USER");                                       // user用户的角色只是USER
    }
}
