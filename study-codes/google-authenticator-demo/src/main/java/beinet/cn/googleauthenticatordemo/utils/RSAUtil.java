package beinet.cn.googleauthenticatordemo.utils;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA非对称加密算法实现类.
 * 用于对数据进行加密和解密
 *
 * @author youbl
 * @since 2023/9/26 15:43
 */
public final class RSAUtil {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_LEN = 2048;

    private static final Charset UTF8 = StandardCharsets.UTF_8;//Charset.forName("UTF-8");


    private RSAUtil() {
    }

    public static ACKeys generateKeyPair() {
        KeyPair keys = generateKeyPairInner();
        return new ACKeys()
                .setPrivateKey(encodeKey(keys.getPrivate()))
                .setPublicKey(encodeKey(keys.getPublic()));
    }

    /**
     * 使用公钥加密.
     * 注：每2次调用，会生成2个不同的签名
     *
     * @param publicKey 公钥
     * @param data      要加密的数据
     * @return 加密结果
     */
    public static String encrypt(String publicKey, String data) {
        PublicKey key = decodePublicKey(publicKey);
        byte[] byteData = data.getBytes(UTF8);
        byte[] result = encrypt(key, byteData);
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 使用私钥解密
     *
     * @param privateKey 私钥
     * @param data       要解密的数据
     * @return 解密结果
     */
    public static String decrypt(String privateKey, String data) {
        PrivateKey key = decodePrivateKey(privateKey);
        byte[] byteData = Base64.getDecoder().decode(data);
        return new String(decrypt(key, byteData), UTF8);
    }

    /**
     * 使用公钥加密数据
     *
     * @param key  公钥
     * @param data 要加密的数据
     * @return 加密结果
     */
    @SneakyThrows
    private static byte[] encrypt(PublicKey key, byte[] data) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 使用私钥解密
     *
     * @param key  私钥
     * @param data 要解密的数据
     * @return 解密结果
     */
    @SneakyThrows
    private static byte[] decrypt(PrivateKey key, byte[] data) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }


    /**
     * 获取一对 公钥/私钥
     *
     * @return 公钥/私钥
     */
    @SneakyThrows
    private static KeyPair generateKeyPairInner() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_LEN);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 返回baseb4字符串形式的 密钥
     *
     * @param key 公钥或私钥
     * @return 字符串
     */
    private static String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 把base64形式的密钥转换为公钥返回
     *
     * @param keyStr 密钥
     * @return 公钥
     */
    @SneakyThrows
    private static PublicKey decodePublicKey(String keyStr) {
        byte[] bytes = Base64.getDecoder().decode(keyStr);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 把base64形式的密钥转换为私钥返回
     *
     * @param keyStr 密钥
     * @return 私钥
     */
    @SneakyThrows
    private static PrivateKey decodePrivateKey(String keyStr) {
        byte[] bytes = Base64.getDecoder().decode(keyStr);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        return keyFactory.generatePrivate(keySpec);
    }
}
