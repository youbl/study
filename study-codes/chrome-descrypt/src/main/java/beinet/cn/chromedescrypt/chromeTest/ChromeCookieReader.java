package beinet.cn.chromedescrypt.chromeTest;

import beinet.cn.chromedescrypt.chromeTest.dto.CookieDto;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 从Windows Chrome存储文件中读取Cookie数据
 *
 * @author youbl
 * @since 2024/6/14 13:05
 */
public class ChromeCookieReader {
    /**
     * Cookie存储的sqlite文件路径.
     * 通常存储在 "%LocalAppData%\Google\Chrome\User Data\Default\Network\Cookies"
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
    public ChromeCookieReader(String loginDataFilePath, String localStateFilePath) {
        this.loginDataFilePath = loginDataFilePath;
        this.localStateFilePath = localStateFilePath;
    }

    public List<CookieDto> queryData() {
        // 先获取解密用的密钥
        byte[] key = new ChromePwdDecoder(localStateFilePath).readPwdKey();

        // 查询所有存储的Cookie
        // 注：时间字段 creation_utc expires_utc last_access_utc last_update_utc 不是时间戳
        //    对应的时间转换语法是 datetime(字段名 / 1000000 + (strftime('%s', '1601-01-01')), 'unixepoch', 'localtime')
        String sql = "SELECT * FROM cookies";
        List<Map<String, Object>> data = getJdbcTemplate().queryForList(sql);
        List<CookieDto> ret = new ArrayList<>();
        for (Map<String, Object> row : data) {
            byte[] encryptedCookie = (byte[]) row.get("encrypted_value");
            // 解密cookie
            String cookie = decryptPassword(encryptedCookie, key);
            // 转换时间
            String expireTime = convertChromeTime((Long) row.get("expires_utc"));
            CookieDto dto = new CookieDto()
                    .setDomain(row.get("host_key").toString())
                    .setExpireTime(expireTime)
                    .setHttpOnly((Integer) row.get("is_httponly"))
                    .setName(row.get("name").toString())
                    .setPath(row.get("path").toString())
                    .setPort((Integer) row.get("source_port"))
                    .setSecure((Integer) row.get("is_secure"))
                    .setValue(cookie);
            ret.add(dto);
        }
        return ret;
    }

    // Chrome的时间比较特殊，并不是时间戳格式
    private String convertChromeTime(Long time) {
        if (time == null)
            return "";
        // Convert expires_utc to LocalDateTime
        LocalDateTime utcDateTime = LocalDateTime.of(1601, 1, 1, 0, 0)
                .plusSeconds(time / 1000000);

        // Convert UTC LocalDateTime to local timezone
        LocalDateTime localDateTime = utcDateTime.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneOffset.systemDefault())
                .toLocalDateTime();

        // Format localDateTime as desired
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @SneakyThrows
    private String decryptPassword(byte[] password, byte[] key) {
        if (password == null || password.length == 0)
            return "";
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
