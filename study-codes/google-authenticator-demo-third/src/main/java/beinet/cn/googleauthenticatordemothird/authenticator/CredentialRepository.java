package beinet.cn.googleauthenticatordemothird.authenticator;

import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 13:28
 */
@Component
public class CredentialRepository implements ICredentialRepository {
    // todo: 实际项目中，这个map要改用mysql数据库进行存储，确保每个用户只创建并关联一个secretKey
    private final Map<String, UserTOTP> usersKeys = new HashMap<String, UserTOTP>();

    /**
     * todo: 实际项目中，这个方法要从mysql数据库中读取，确保每个用户只创建并关联一个secretKey
     *
     * @param userName 登录用户
     * @return 该用户关联的secretKey
     */
    @Override
    public String getSecretKey(String userName) {
        UserTOTP userTOTP = usersKeys.get(userName);
        return userTOTP == null ? "" : userTOTP.getSecretKey();
    }

    /**
     * 保存指定用户的secretKey
     * todo: 实际项目中，这些数据要写入mysql数据库中
     *
     * @param userName       登录用户
     * @param secretKey      为该用户分配的secretKey
     * @param validationCode the validation code.
     * @param scratchCodes   the list of scratch codes.
     */
    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        usersKeys.put(userName, new UserTOTP(userName, secretKey, validationCode, scratchCodes));
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UserTOTP {
        private String username;
        private String secretKey;
        private int validationCode;
        private List<Integer> scratchCodes;
    }
}
