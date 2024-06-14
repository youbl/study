package beinet.cn.chromedescrypt.chromeTest.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 *
 * @author youbl
 * @since 2024/6/14 11:37
 */
@Data
@Accessors(chain = true)
public class UserDataDto {
    private String action_url;
    private String username;
    private String password;
}
