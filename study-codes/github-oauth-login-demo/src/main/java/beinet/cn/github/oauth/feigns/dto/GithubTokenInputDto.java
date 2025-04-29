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
public class GithubTokenInputDto {
    private String client_id;
    private String client_secret;
    private String code;
}
