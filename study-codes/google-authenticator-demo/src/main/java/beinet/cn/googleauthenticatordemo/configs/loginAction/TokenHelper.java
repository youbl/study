package beinet.cn.googleauthenticatordemo.configs.loginAction;

import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 新类
 *
 * @author youbl
 * @date 2023/3/27 15:45
 */
public final class TokenHelper {
    // token 名
    public static final String TOKEN_NAME = "token";

    // token cookie名
    public static final String TOKEN_COOKIE_NAME = "beinetUser";

    // token校验md5的盐值
    private static final String TOKEN_SALT = "beinet.cn";

    // token不同信息的分隔符
    private static final String TOKEN_SPLIT = ":";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 校验token格式和md5是否正确
     *
     * @param token token
     * @return 是否正确token
     */
    public static String getLoginUserFromToken(String token) {
        if (!StringUtils.hasLength(token)) {
            return null;
        }
        // 0为用户名，1为时间，2为md5
        String[] arr = token.split(TOKEN_SPLIT);
        if (arr.length != 3 || arr[0].isEmpty() || arr[1].isEmpty() || arr[2].isEmpty()) {
            return null;
        }
        long loginTime = Long.parseLong(arr[1]);
        long now = Long.parseLong(LocalDateTime.now().format(FORMATTER));
        long diff = now - loginTime;
        // 1000000为1天，要求每天登录
        if (diff < 0 || diff > 1000000) {
            return null;
        }

        String countToken = buildToken(arr[0], arr[1]);
        if (countToken.equalsIgnoreCase(token)) {
            return arr[0];
        }
        return null;
    }

    public static String buildToken(String username) {
        String date = LocalDateTime.now().format(FORMATTER);
        return buildToken(username, date);
    }

    /**
     * 根据用户名和登录时间，计算md5，并拼接token返回
     *
     * @param username 用户名
     * @param date     登录时间
     * @return token
     */
    public static String buildToken(String username, String date) {
        if (date == null) {
            date = LocalDateTime.now().format(FORMATTER);
        }
        String ret = username + TOKEN_SPLIT + date + TOKEN_SPLIT;
        String md5Source = ret + "-" + TOKEN_SALT + "-" + username;

        String salt = "$1$" + TOKEN_SALT + "$"; // 盐值的格式必须为 "$id$salt$"
        String md5 = Md5Crypt.md5Crypt(md5Source.getBytes(StandardCharsets.UTF_8), salt);
        return ret + md5;
    }
}
