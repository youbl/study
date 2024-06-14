package beinet.cn.chromedescrypt.chromeTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.win32.Crypt32Util;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;

/**
 * 从Windows Chrome的Local State中解析解密密钥的类
 *
 * @author youbl
 * @since 2024/6/13 21:05
 */
public class ChromePwdDecoder {
    private String localStateFilePath;

    /**
     * 构造函数
     *
     * @param localStateFilePath Local State文件路径
     */
    public ChromePwdDecoder(String localStateFilePath) {
        this.localStateFilePath = localStateFilePath;
    }

    /**
     * 读取Local State里的密钥值.
     * 注：解密用的密码文件 "%LocalAppData%\Google\Chrome\User Data\Local State"
     * 该文件内容为json格式，密码在 os_crypt.encrypted_key
     *
     * @return 密钥值
     */
    @SneakyThrows
    public byte[] readPwdKey() {
        String fileName = localStateFilePath;
        // 加载文件内容
        String fileContent = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);

        // 读取 os_crypt.encrypted_key 值并返回
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(fileContent);
        String pwd = rootNode.path("os_crypt").path("encrypted_key").asText();
        return decodePwd(pwd);
    }

    /**
     * 把Local State里读取出的密钥，解码后返回
     *
     * @param key 密钥
     * @return 解码后的密钥
     */
    private byte[] decodePwd(String key) {
        byte[] arr = Base64.getDecoder().decode(key);
        // 移除前面的无用字符：DPAPI
        arr = Arrays.copyOfRange(arr, 5, arr.length);
        byte[] keys = Crypt32Util.cryptUnprotectData(arr);
        System.out.println(keys);
        return keys;
    }
}
