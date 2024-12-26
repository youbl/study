package beinet.cn.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 * @author youbl
 * @since 2024/12/26 16:04
 */
@Data
@Accessors(chain = true)
public class GoogleUser {
    // Google用户id
    private String id;
    // Google用户邮箱
    private String email;
    // Google用户
    private Boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
}
