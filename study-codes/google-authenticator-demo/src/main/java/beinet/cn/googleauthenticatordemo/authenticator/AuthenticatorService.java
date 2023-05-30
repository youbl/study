package beinet.cn.googleauthenticatordemo.authenticator;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 15:56
 */
@Service
public class AuthenticatorService {
    private Map<String, String> userKeys = new HashMap<>();

    /**
     * 生成一个secretKey，并关联到用户，
     * 然后返回二维码字符串
     *
     * @param username 用户名
     * @return 二维码字符串
     */
    public String generateAuthUrl(String username) {
        String secret = GoogleGenerator.generateSecretKey();
        // todo: 实际项目中，用户名与secretKey的关联关系应当存储在数据库里，否则变化了，就会无法登录
        userKeys.put(username, secret);
        return GoogleGenerator.getQRBarcode(username, secret);
    }

    /**
     * 根据用户名和输入的code，进行校验并返回成功失败
     *
     * @param username 用户名
     * @param code     输入的code
     * @return 校验成功与否
     */
    public boolean validateCode(String username, int code) {
        // todo: 从数据库里读取该用户的secretKey
        String secret = userKeys.get(username);
        if (!StringUtils.hasLength(secret)) {
            throw new RuntimeException("该用户未使用Google身份验证器注册，请先注册");
        }

        return GoogleGenerator.checkCode(secret, code);
    }
}
