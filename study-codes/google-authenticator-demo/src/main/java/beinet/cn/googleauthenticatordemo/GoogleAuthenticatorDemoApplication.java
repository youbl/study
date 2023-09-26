package beinet.cn.googleauthenticatordemo;

import beinet.cn.googleauthenticatordemo.utils.ECDSAUtil;
import beinet.cn.googleauthenticatordemo.utils.RSAUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleAuthenticatorDemoApplication implements CommandLineRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(GoogleAuthenticatorDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        signAndCheck();

        encryptAndDecrypt();
    }

    // 用私钥对数据做签名，用公钥对签名进行验证
    private void signAndCheck() {
        // 获取公钥私钥对的代码
//        ACKeys keys = ECDSAUtil.generateKeyPair();
//		System.out.println(keys.getPrivateKey());
//        System.out.println(keys.getPublicKey());
        String privateKey = "MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQgPA41tNdZLc5j4IHzhNS4/Tm3QaYGOVyCg9ERul6LR3WgBwYFK4EEAAqhRANCAAQAITwOPdwhbfAFJHbjAGIX2dYFyUsiYrLGWEFuCHapGfLo1abmLWQ72iOD0PATdKa2NyQ7649Vx6q7uoelJIyk";
        String publicKey = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEACE8Dj3cIW3wBSR24wBiF9nWBclLImKyxlhBbgh2qRny6NWm5i1kO9ojg9DwE3SmtjckO+uPVcequ7qHpSSMpA==";

        String signData = "abcde张三法外狂徒在逛街db332w4lk";

        // 用私钥生成签名
        String sign = ECDSAUtil.sign(privateKey, signData);
        System.out.println(sign);
        sign = ECDSAUtil.sign(privateKey, signData);
        System.out.println(sign);
        sign = ECDSAUtil.sign(privateKey, signData);
        System.out.println(sign);
        //String sign = "MEUCIEROGvPu3D8SPkx7UGMPiGXvn8o/1dbeAFSXkuWQxeneAiEA0057UQErnmJGonUmJOu7Vt2L7XCqjOTVPOQgRFD4V9U=";
        //String sign = "MEUCIQDbv6K5Ky1NdvrR0/InOOoe//pjMXZ7Ks1wLAJHhKwSZwIgN2ncNEdDr3gcxGg8Hm9tfHpVsMSO3vdpyHqbSYljLCk=";

        // 用公钥验证签名
        boolean check = ECDSAUtil.verify(publicKey, signData, sign);
        System.out.println(check);
    }

    // 用公钥加密数据，用私钥解密数据
    private void encryptAndDecrypt() {
        // 获取RSA的公钥私钥对的代码
//        ACKeys keys = RSAUtil.generateKeyPair();
//        System.out.println(keys.getPrivateKey());
//        System.out.println(keys.getPublicKey());
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCsIdWMn0362EYnjmOEdI5TQ+69BhEtRpk9sIxtF3uS5mYOI0mbRDu5yqeW+ueZoGNkysoj5pGJFAnOrx5brepfrT9FOHAz4t7m252xNF4RpjxUsHQrAlvgNiflnV3kB+OvoTwHMmxmRfR945YuLsgdfCeR6nOcOOztZgRWKZBnjFEI67jPnjSOd3yk8cATPsSDLvOMammC4vbrlO58WMDcsVtvy/Rz2gwZpxHmOhedW6UhPvP8ZsEXmIOUTi83s8WOnFlPT6DDqZvkd6SU+bHZpFl10E0HBUBzVf67DvASjwUJ5Fpr5DCogIiv8RcdOx9/vg0il+6fJN8JbQycGSfnAgMBAAECggEAbJ5RDhJ8d5y/8DgWrj+KjBpi7R6F+kcBbf8ZLcHStajzn1o/GV4ePigWRtx/Bt9BuRmKIstc7N4bLjQs+Pcu3T8KnNh3XRZ9R7ovsOF1trdqfJp4Q6eSaFJkLK130Ov1jh81LOlxo7vjAwl4/B2DI6/znE0QtPTq9QcaF3DU8bF8ix+sDazI5YlexUrT+I/QSUwFzcHRf8ULQ9MfPdiD4cY8ii3o4DrCxtQNFbQBIrvrwtEaZu6Hb12EtV0bhf3/VEndIerUNE5IrixCwm6o0fsSCF5+JMMRVAnJZebJoaQ28NVjjAVFpefYbNtNH3RGu4Id5LAVnGLWdmrdCyevGQKBgQDk0h02ERwT0xOQXdgJ5wtkx9bBrvSnv4Ncz3Rcc7VvMnCwU9zl9DCC+HKzqAtXW1GJcMWiWInwhoTlL8egCc61qLh5QAqkkhwrMJwwRWAnhDf8wmW+1K/3qqa8mc8wofE7jRTl6trDZjROmzL2gvLtYrhDszWdaZQYsvMQcBX9SwKBgQDAk/UAPPCoi5ZPZIzZgSbL04JxZ1nxOHjYiKDjiqqzFH79jIuHUfYkAFKe5V0UimKdMsSsD8dOrRzhA3G5Mszq/VmmOy8Il4dORO79yX8jHNN2JxQsO8Wk2gXezvKhxGJuSqxN37/k7DGbRUhVCtAlYy83UG7DmmcdbXccIeFqVQKBgGKOrc5P6EwRqmeQRDFftuSdvr6F4RMvxiD/qhr4K7nNfAQ22vqhBxQidyUyyE/EIYaVcErQ3/kdt7gPsGgaUVKlbW+sSelAlal0spGIEDZ1GLhh8vkTGwgOFOs/RKKp3gHMbqfpVHi9WOJSrszG8mOVFdDzK7C1fPh2e4MlBBOpAoGBAIzhsLJj4orq0yNxo7jays8CBMOBoELf1UhTa6AKqaYosF48mzZ7t13O/qJPmTCVrF7j7Q0S3fOooR/brS6XA2JFxXfehCH8+HdB5rSy29eMFwawwAk8EPLwrKq7KrU9PeUBq9usfG20fPXiUXAlYPB5BumoZd+i7RySIDjscEOhAoGBAIPCTEhQFySqhLatm/YvKGnpWbz89AwBi5obqSMb4UaljUwAmObPgXi3wm1B8uUveBG9l2QK9KyQHn6/2w/Fm6RUiA8c9wDktp6Xewc93bvrocqG4UGLeqV8/W3bHzRroMXDkLWQxyZ1XTfSPsRdWlYm6SvILD+/T6L21fx6SgjA";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArCHVjJ9N+thGJ45jhHSOU0PuvQYRLUaZPbCMbRd7kuZmDiNJm0Q7ucqnlvrnmaBjZMrKI+aRiRQJzq8eW63qX60/RThwM+Le5tudsTReEaY8VLB0KwJb4DYn5Z1d5Afjr6E8BzJsZkX0feOWLi7IHXwnkepznDjs7WYEVimQZ4xRCOu4z540jnd8pPHAEz7Egy7zjGppguL265TufFjA3LFbb8v0c9oMGacR5joXnVulIT7z/GbBF5iDlE4vN7PFjpxZT0+gw6mb5HeklPmx2aRZddBNBwVAc1X+uw7wEo8FCeRaa+QwqICIr/EXHTsff74NIpfunyTfCW0MnBkn5wIDAQAB";
//
        String data = "abcde张三法外狂徒在逛街db332w4lk";
        String encryptData = RSAUtil.encrypt(publicKey, data);
        System.out.println(encryptData);

        encryptData = RSAUtil.encrypt(publicKey, data);
        System.out.println(encryptData);

        String decryptData = RSAUtil.decrypt(privateKey, encryptData);
        System.out.println(decryptData);
        System.out.println(decryptData.equals(data));
    }
}
