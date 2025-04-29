package beinet.cn.github.oauth.feigns.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 * @author youbl
 * @since 2025/4/29 16:59
 */
@Data
@Accessors(chain = true)
public class GithubTokenOutputDto {
    private String error;
    private String error_description;
    private String error_uri;

    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private String token_type;
    private String scope;

    public boolean success() {
        return error == null || error.isEmpty();
    }
}
