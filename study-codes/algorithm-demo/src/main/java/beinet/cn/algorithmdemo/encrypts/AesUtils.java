package beinet.cn.algorithmdemo.encrypts;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密解密辅助类
 *
 * @author youbl
 * @date 2023/4/14 14:21
 */
public final class AesUtils {
    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int IV_LENGTH = 16;

    /**
     * AES加密，返回的结果密文是固定的
     *
     * @param plaintext 明文
     * @param key       密钥
     * @param iv        初始化向量，为空时使用key
     * @return 密文
     */
    public static String encrypt(String plaintext, String key, String iv) {
        byte[] ciphertextBytes = encryptInner(plaintext, key, iv);
        return Base64Utils.encodeToString(ciphertextBytes);//通过Base64转码返回
    }

    /**
     * AES解密
     *
     * @param ciphertext 密文
     * @param key        密钥
     * @param iv         初始化向量，为空时使用key
     * @return 明文
     */
    public static String decrypt(String ciphertext, String key, String iv) {
        iv = StringUtils.hasLength(iv) ? iv : key;
        iv = fixKey(iv);

        byte[] ciphertextBytes = Base64Utils.decodeFromString(ciphertext);
        byte[] ivBytes = iv.getBytes(CHARSET);
        return decryptInner(ciphertextBytes, key, ivBytes);
    }


    /**
     * AES加密，注意：返回的结果密文是动态的，因为IV是随机生成。
     * 只能使用decryptRndIV解密
     *
     * @param plaintext 明文
     * @param key       密钥
     * @return 密文
     */
    public static String encryptRndIV(String plaintext, String key) {
        byte[] ivBytes = generateIv();  // 随机IV
        byte[] ciphertextBytes = encryptInner(plaintext, key, ivBytes);

        // 把IV也打印在输出的密文里，方便解密时提取
        byte[] combinedBytes = new byte[ivBytes.length + ciphertextBytes.length];
        System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
        System.arraycopy(ciphertextBytes, 0, combinedBytes, ivBytes.length, ciphertextBytes.length);
        return Base64.getEncoder().encodeToString(combinedBytes);
    }

    /**
     * AES解密，注意：只能接收encryptRndIV生成的密文进行解密
     *
     * @param ciphertext 密文
     * @param key        密钥
     * @return 明文
     */
    public static String decryptRndIV(String ciphertext, String key) {
        byte[] combinedBytes = Base64.getDecoder().decode(ciphertext);
        byte[] ivBytes = new byte[IV_LENGTH];
        byte[] ciphertextBytes = new byte[combinedBytes.length - IV_LENGTH];
        System.arraycopy(combinedBytes, 0, ivBytes, 0, IV_LENGTH);
        System.arraycopy(combinedBytes, IV_LENGTH, ciphertextBytes, 0, ciphertextBytes.length);
        return decryptInner(ciphertextBytes, key, ivBytes);
    }

    private static byte[] encryptInner(String plaintext, String key, String iv) {
        iv = StringUtils.hasLength(iv) ? iv : key;
        iv = fixKey(iv);
        return encryptInner(plaintext, key, iv.getBytes(CHARSET));
    }

    private static byte[] encryptInner(String plaintext, String key, byte[] ivBytes) {
        key = fixKey(key);
        byte[] plaintextBytes = plaintext.getBytes(CHARSET);
        byte[] keyBytes = key.getBytes(CHARSET);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM), new IvParameterSpec(ivBytes));
            return cipher.doFinal(plaintextBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String decryptInner(byte[] ciphertextBytes, String key, byte[] ivBytes) {
        key = fixKey(key);
        byte[] keyBytes = key.getBytes(CHARSET);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM), new IvParameterSpec(ivBytes));

            byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
            return new String(plaintextBytes, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] generateIv() {
        byte[] ivBytes = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(ivBytes);
        return ivBytes;
    }

    /**
     * 截断或补充key，确保返回的是16位字符串。
     * 因为AES要求必须是16位的key,否则报错：Invalid AES key length: 9 bytes
     *
     * @param key 源
     * @return 16位key
     */
    private static String fixKey(String key) {
        if (!StringUtils.hasLength(key)) {
            throw new RuntimeException("key can't be empty.");
        }
        int len = key.length();
        if (len == IV_LENGTH) return key;

        while (len < IV_LENGTH) {
            key += key;
            len += len;
        }
        return key.substring(0, 16);
    }
}
