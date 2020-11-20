package beinet.cn.demospringsecurity.security;

import beinet.cn.demospringsecurity.security.argument.AuthDetailArgumentResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 登录相关的配置，如：哪些地址要登录，角色是啥，登录地址、登录成功/失败操作等等
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class BeinetAuthConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsService service;

    public BeinetAuthConfiguration(@Qualifier("beinetUserService") UserDetailsService service) {
        this.service = service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); 即默认配置是下面这一句
        //((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();

        // 先关闭csrf，影响登录和退出
        http.csrf().disable();

        // antMatchers 规则：
        // ? 匹配1个字符
        // * 匹配0或多个字符
        // ** 匹配0或多个目录
        // {varName:[a-z]+} 匹配正则，并把正则匹配到的内容赋值给变量
        // 参考文档 https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html
        http
                // Set-Cookie: remember-me=ck94QXZMWG9zQW1PdjdGazB5WG55USUzRCUzRDpzaUp3aWx2YVNPcHh3MEN5UzNFOW5nJTNEJTNE; Max-Age=3600; Expires=Tue, 15-Sep-2020 03:38:47 GMT; Path=/; HttpOnly
                .rememberMe()   // 开启记住我功能 AbstractRememberMeServices.autoLogin 这里读取Cookie并自动登录
                .rememberMeParameter("beinetRemember")  // 修改记住我的参数名
                .tokenValiditySeconds(3600)             // 过期时间1小时，不配置时，默认为2星期
                .tokenRepository(new BeinetPersistentTokenRepository()) // 读写token的持久层，用于保存数据和恢复数据
                .userDetailsService(this.service)       // 必须单独为记住我配置userService
                .and()
                .formLogin()                        // 开启form表单登录
                .usernameParameter("beinetUser")    // 修改登录用户名参数
                .passwordParameter("beinetPwd")     // 修改登录密码参数
                .loginPage("/myLogin.html")         // 使用自定义的登录表单
                .loginProcessingUrl("/login")       // 接收POST登录请求的处理地址
                .successHandler(new BeinetHandleSuccess()) // 登录验证通过后的处理器
                .failureHandler(new BeinetHandleFail())    // 登录验证失败后的处理器
                .permitAll()                        // 允许上述请求匿名访问, 注：不加这句，会导致302死循环
                .and()                              // 把前面的返回结果，转换回HttpSecurity，以便后续的流式操作
                .logout()                           // 开启退出登录接口
                //.logoutUrl("/logout")             // 指定退出登录的url，默认就是/logout
                .logoutSuccessHandler(new BeinetHandleLogout()) // 退出成功后的处理器
                .permitAll()                        // 退出的url要允许匿名访问
                .and()
                .exceptionHandling()                // 开始异常处理配置
                .authenticationEntryPoint(new BeinetAuthenticationEntryPoint()) // 指定匿名访问需登录的url的异常处理器
                .accessDeniedHandler(new BeinetHandleAccessDenied())            // 指定登录用户访问无权限url的异常处理器
                .and()
                .authorizeRequests()                // 开始指定请求授权
                .antMatchers("/myLogin.html/**").permitAll() // 上面的loginPage("/myLogin.html").permitAll() 居然不支持带参数: myLogin.html?xxx
                .antMatchers("/res/**").permitAll()     // res根路径及子目录请求，不限制访问
                .antMatchers("/news/**").hasRole("ROOT")// news根路径及子目录请求，要求ROOT角色才能访问
                .antMatchers("/time/**").hasRole("USER")// time根路径及子目录请求，要求USER角色才能访问
                .antMatchers("/role/**").permitAll()    // 忽略配置里的权限，改用 EnableGlobalMethodSecurity 和 PreAuthorize 注解
                .anyRequest().authenticated();      // 其它所有请求都要求登录后访问，但是不限制角色
    }


    @Configuration
    static class WebMvcConfig implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new AuthDetailArgumentResolver());
        }
    }
}
