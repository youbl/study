package beinet.cn.github.oauth.feigns.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Github /user/emails返回的数组元素
 * @author youbl
 * @since 2025/5/21 11:04
 */
@Data
@Accessors(chain = true)
public class GithubEmailDto {
    private String email;
    private Boolean primary;
    private Boolean verified;
    private String visibility;
}
