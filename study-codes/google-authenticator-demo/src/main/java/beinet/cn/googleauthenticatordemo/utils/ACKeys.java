package beinet.cn.googleauthenticatordemo.utils;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 非对称密码（asymmetric cipher）的公私钥对
 *
 * @author youbl
 * @since 2023/9/26 17:25
 */
@Data
@Accessors(chain = true)
public class ACKeys {
    private String privateKey;
    private String publicKey;
}