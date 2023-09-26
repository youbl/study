package beinet.cn.googleauthenticatordemo.utils;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 基于ECDSA的非对称签名算法实现类。
 * 注：ECDSA是基于椭圆曲线数字签名算法，对数据进行签名的一种算法。
 *
 * @author youbl
 * @since 2023/9/26 15:43
 */
public final class ECDSAUtil {

    private static final String PROVIDER = "BC";
    private static final String ALGORITHM_KEY = "ECDSA";

    private static final String ALGORITHM_SIGN = "SHA256withECDSA";

    private static final Charset UTF8 = StandardCharsets.UTF_8;


    private ECDSAUtil() {
    }

    static {
        // 必须执行，否则会报错 no such provider: BC
        Security.addProvider(new BouncyCastleProvider());
    }

    public static ACKeys generateKeyPair() {
        KeyPair keys = generateKeyPairInner();
        return new ACKeys()
                .setPrivateKey(encodeKey(keys.getPrivate()))
                .setPublicKey(encodeKey(keys.getPublic()));
    }

    /**
     * 使用私钥计算签名.
     * 注：每2次调用，会生成2个不同的签名
     *
     * @param privateKey 私钥
     * @param data       要加密的数据
     * @return 签名结果
     */
    public static String sign(String privateKey, String data) {
        PrivateKey key = decodePrivateKey(privateKey);
        byte[] byteData = data.getBytes(UTF8);
        byte[] result = sign(key, byteData);
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 使用公钥验证签名
     *
     * @param publicKey 公钥
     * @param data      要解密的数据
     * @param signature 提供的签名数据
     * @return 验证通过与否
     */
    public static boolean verify(String publicKey, String data, String signature) {
        PublicKey key = decodePublicKey(publicKey);
        byte[] byteData = data.getBytes(UTF8);
        byte[] signData = Base64.getDecoder().decode(signature);
        return verify(key, byteData, signData);
    }

    /**
     * 使用私钥计算签名
     *
     * @param privateKey 私钥
     * @param data       要加密的数据
     * @return 签名结果
     */
    @SneakyThrows
    private static byte[] sign(PrivateKey privateKey, byte[] data) {
        Signature signature = Signature.getInstance(ALGORITHM_SIGN, PROVIDER);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 使用公钥验证签名
     *
     * @param publicKey 公钥
     * @param data      要解密的数据
     * @param signature 提供的签名数据
     * @return 验证通过与否
     */
    @SneakyThrows
    private static boolean verify(PublicKey publicKey, byte[] data, byte[] signature) {
        Signature verifySignature = Signature.getInstance(ALGORITHM_SIGN, PROVIDER);
        verifySignature.initVerify(publicKey);
        verifySignature.update(data);
        return verifySignature.verify(signature);
    }


    /**
     * 获取一对 公钥/私钥
     *
     * @return 公钥/私钥
     */
    @SneakyThrows
    private static KeyPair generateKeyPairInner() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_KEY, PROVIDER);
        keyPairGenerator.initialize(new ECGenParameterSpec("secp256k1"));
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
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_KEY, PROVIDER);
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
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_KEY, PROVIDER);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        return keyFactory.generatePrivate(keySpec);
    }
}
