package beinet.cn.github.oauth;

import beinet.cn.github.oauth.feigns.GithubApiFeign;
import beinet.cn.github.oauth.feigns.GithubTokenFeign;
import beinet.cn.github.oauth.feigns.dto.GithubEmailDto;
import beinet.cn.github.oauth.feigns.dto.GithubTokenInputDto;
import beinet.cn.github.oauth.feigns.dto.GithubTokenOutputDto;
import beinet.cn.github.oauth.feigns.dto.GithubUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        GithubTokenOutputDto token = githubTokenFeign.getAccessToken(dto);
        if (!token.success()) {
            throw new RuntimeException("failed: " + token.getError_description() + " " + token.getError_uri());
        }

        String auth = token.getToken_type() + " " + token.getAccess_token();
        // 根据access_token, 获取用户信息
        GithubUserDto ret = githubApiFeign.getUserInfo(auth);

        if (!StringUtils.hasLength(ret.getEmail())) {
            // github的user接口会按用户设置显示或隐藏邮箱，因此如果没取到邮箱，要主要调用这个接口重新获取邮箱
            // 参考 https://stackoverflow.com/questions/35373995/github-user-email-is-null-despite-useremail-scope
            // 注意要在github后台添加Email的只读权限
            ret.setEmail(getPrimaryEmail(auth));
        }
        return ret;
    }

    private String getPrimaryEmail(String auth) {
        List<GithubEmailDto> result = githubApiFeign.getUserEmails(auth);
        if (result == null || result.isEmpty()) {
            return "";
        }
        String email = "";
        for (GithubEmailDto item : result) {
            if (item == null || !StringUtils.hasLength(item.getEmail())) {
                continue;
            }
            email = item.getEmail();
            if (item.getPrimary() != null && item.getPrimary()) {
                // 优先返回Primary的邮箱
                return email;
            }
        }
        return email;
    }
}
