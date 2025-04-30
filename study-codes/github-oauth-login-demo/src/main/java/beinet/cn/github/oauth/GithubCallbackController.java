package beinet.cn.github.oauth;

import beinet.cn.github.oauth.feigns.GithubApiFeign;
import beinet.cn.github.oauth.feigns.GithubTokenFeign;
import beinet.cn.github.oauth.feigns.dto.GithubTokenInputDto;
import beinet.cn.github.oauth.feigns.dto.GithubTokenOutputDto;
import beinet.cn.github.oauth.feigns.dto.GithubUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收Github的登录回调
 * @author youbl
 * @since 2025/4/29 16:49
 */
@RestController
@RequiredArgsConstructor
public class GithubCallbackController {
    private final String githubClientId = "Iv23liwz6AxRv7VXcHvf";
    private final String githubClientSecret = "github app里的Client secret";

    private final GithubTokenFeign githubTokenFeign;
    private final GithubApiFeign githubApiFeign;

    // 会带code回调，如 http://localhost:8080/callback?code=abc
    @GetMapping("callback")
    public GithubUserDto callback(@RequestParam String code) {
        GithubTokenInputDto dto = new GithubTokenInputDto()
                .setClient_id(githubClientId)
                .setClient_secret(githubClientSecret)
                .setCode(code);
        // 根据授权码，获取access_token
        GithubTokenOutputDto ret = githubTokenFeign.getAccessToken(dto);
        if (!ret.success()) {
            throw new RuntimeException("failed: " + ret.getError_description() + " " + ret.getError_uri());
        }

        String auth = ret.getToken_type() + " " + ret.getAccess_token();
        // 根据access_token, 获取用户信息
        return githubApiFeign.getUserInfo(auth);
    }
}
