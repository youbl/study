package beinet.cn.springdiffiehellmandemo.demos.web;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2024/04/02 20:43
 */
@RestController
public class ExchangeController {

    @SneakyThrows
    @GetMapping("exchange")
    public String[] exchangePwd(@RequestParam String clientPublicKey) {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
        keyGen.initialize(ecSpec, new SecureRandom());
        // 生成密钥对
        KeyPair serverSideKeyPair = keyGen.generateKeyPair();
        // 获取密钥协商对象
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");

        // 初始化密钥协商对象
        keyAgreement.init(serverSideKeyPair.getPrivate());
        // 执行协商计算
        keyAgreement.doPhase(stringToPublicKey(clientPublicKey), true);

        // 生成协商密钥
        byte[] secretKey = keyAgreement.generateSecret();

        return new String[]{
                new String(secretKey),
                keyToString(serverSideKeyPair.getPublic()),
                keyToString(serverSideKeyPair.getPrivate())
        };
    }

    /**
     * 将字符串转换为PublicKey
     */
    @SneakyThrows
    public static PublicKey stringToPublicKey(String publicKeyString) {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将Key转换为字符串
     */
    public static String keyToString(Key key) {
        byte[] keyBytes = key.getEncoded();
        return new String(Base64.getEncoder().encode(keyBytes));
    }
}
