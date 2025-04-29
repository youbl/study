package beinet.cn.github.oauth.feigns;

import beinet.cn.github.oauth.feigns.dto.GithubTokenInputDto;
import beinet.cn.github.oauth.feigns.dto.GithubTokenOutputDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 获取github的token的接口服务
 * @author youbl
 * @since 2025/4/29 16:54
 */
@FeignClient(name = "github-token", url = "https://github.com")
public interface GithubTokenFeign {
    /**
     * 根据登录成功回调的code，交换access_token
     * @param dto code信息
     * @return token信息
     */
    @PostMapping(value = "login/oauth/access_token", headers = {"Accept=application/json", "Content-Type=application/json"})
    GithubTokenOutputDto getAccessToken(GithubTokenInputDto dto);
    /*
// 异常场景：
// 不加 Accept=application/json ，返回值如下：
// error=bad_verification_code&error_description=The+code+passed+is+incorrect+or+expired.&error_uri=https%3A%2F%2Fdocs.github.com%2Fapps%2Fmanaging-oauth-apps%2Ftroubleshooting-oauth-app-access-token-request-errors%2F%23bad-verification-code
// 加了返回值如下：
// {"error":"bad_verification_code","error_description":"The code passed is incorrect or expired.","error_uri":"https://docs.github.com/apps/managing-oauth-apps/troubleshooting-oauth-app-access-token-request-errors/#bad-verification-code"}
// 正常场景，返回的json如下：
// {access_token=aaa, expires_in=28800, refresh_token=bbb, refresh_token_expires_in=15811200, token_type=bearer, scope=}
    * */
}
