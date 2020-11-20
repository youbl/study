package beinet.cn.demospringsecurity.security.argument;

import lombok.Data;

/**
 * LoginDetails
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/20 10:48
 */
@Data
public class AuthDetails {
    private String userName;
    private String role;

    private String userAgent;
}
