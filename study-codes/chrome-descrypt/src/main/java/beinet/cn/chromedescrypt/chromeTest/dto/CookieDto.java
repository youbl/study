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
public class CookieDto {
    private String domain;
    private Integer port;
    private String name;
    private String value;
    private String path;
    private String expireTime;
    private Integer secure;
    private Integer httpOnly;
}
