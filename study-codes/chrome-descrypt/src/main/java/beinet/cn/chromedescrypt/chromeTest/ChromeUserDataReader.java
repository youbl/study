package beinet.cn.chromedescrypt.chromeTest;

import beinet.cn.chromedescrypt.chromeTest.dto.UserDataDto;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 从Windows Chrome存储的Login Data中读取存储的数据
 *
 * @author youbl
 * @since 2024/6/13 21:05
 */
public class ChromeUserDataReader {
    /**
     * 账号密码文件路径.
     * 通常存储在 "%LocalAppData%\Google\Chrome\User Data\Default\Login Data"
     * 也可以在 chrome://version/ 里查找Profile Path
     */
    private String loginDataFilePath;
    /**
     * 密码解密的密码，所在的文件路径.
     * 通常存储在 %LocalAppData%\Google\Chrome\User Data\Local State" json格式，密码在 os_crypt.encrypted_key
     */
    private String localStateFilePath;

    private JdbcTemplate jdbcTemplate;

    /**
     * 构造函数
     *
     * @param loginDataFilePath  Login Data文件路径
     * @param localStateFilePath Local State文件路径
     */
    public ChromeUserDataReader(String loginDataFilePath, String localStateFilePath) {
        this.loginDataFilePath = loginDataFilePath;
        this.localStateFilePath = localStateFilePath;
    }

    public List<UserDataDto> queryData() {
        byte[] key = new ChromePwdDecoder(localStateFilePath).readPwdKey();

        List<Map<String, Object>> data = getJdbcTemplate().queryForList("SELECT action_url, username_value, password_value FROM logins");
        List<UserDataDto> ret = new ArrayList<UserDataDto>();
        for (Map<String, Object> row : data) {
            byte[] pwd = (byte[]) row.get("password_value");
            String decryptPwd = decryptPassword(pwd, key);
            UserDataDto dto = new UserDataDto()
                    .setAction_url(row.get("action_url").toString())
                    .setUsername(row.get("username_value").toString())
                    .setPassword(decryptPwd);
            ret.add(dto);
        }
        return ret;
    }

    @SneakyThrows
    private String decryptPassword(byte[] password, byte[] key) {
        byte[] iv = Arrays.copyOfRange(password, 3, 15);
        byte[] payload = Arrays.copyOfRange(password, 15, password.length);

        byte[] result = getCipher(key, iv).doFinal(payload);
        return new String(result);
    }

    @SneakyThrows
    private Cipher getCipher(byte[] key, byte[] iv) {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        return cipher;
    }

    private List<Map<String, Object>> queryData(String sql) {
        return getJdbcTemplate().queryForList(sql);
    }

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            //dataSource.setDriverClassName(env.getProperty("xxx.driver-class-name"));
            String url = "jdbc:sqlite:" + loginDataFilePath;
            dataSource.setUrl(url);

            //创建JdbcTemplate对象，设置数据源
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }
}
