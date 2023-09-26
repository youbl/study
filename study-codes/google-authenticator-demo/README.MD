程序工作原理说明：
- beinet.cn.googleauthenticatordemo.configs.LoginFilter  
这个类继承于OncePerRequestFilter，它会拦截当前项目的所有web请求，并进行处理
- beinet.cn.googleauthenticatordemo.configs.loginValidator.Validator   
这个接口用于验证每个请求，是否认证通过，可以正常进入后续处理流程  
在LoginFilter里注入Validator的实现类：
  - beinet.cn.googleauthenticatordemo.configs.loginValidator.NoNeedLoginValidator  
  这个类判断当前请求是否需要登录，不用登录时跳过，要登录的返回false：
    - 判断是否静态资源，如js/css/图片等，这些资源不需要判断是否登录
    - 判断是否login请求，这个是登录验证的接口，访问它当然不需要判断是否登录
    - actuator这类接口一般用于监控和心跳，不需要判断是否登录
    - test开头的接口，测试场景，不需要判断是否登录
  - beinet.cn.googleauthenticatordemo.configs.loginValidator.CookieValidator  
  这个类判断是否登录通过，不通过时返回false：
    - 是否存在账号密码验证通过的cookie，并判断cookie是否有效
    - 是否需要进行MFA认证，比如otp开头的请求，未MFA认证通过也要能访问
    - 是否存在MFA认证通过的cookie，并判断cookie是否有效
- beinet.cn.googleauthenticatordemo.configs.loginAction.LoginController  
login登录请求类，提供了3个API接口：
  - login/imgcode 显示图形验证码
  - login 校验图形验证码和账号密码
  - logout 清空cookie，达到退出登录的效果
- beinet.cn.googleauthenticatordemo.configs.loginAction.OtpController  
MFA验证请求类，提供了2个API接口：
  - otp/status 获取MFA绑定状态，为false时要返回密钥，让前端进行扫码绑定
  - otp/valid 根据登录用户，计算mfa code，与用户输入进行对比