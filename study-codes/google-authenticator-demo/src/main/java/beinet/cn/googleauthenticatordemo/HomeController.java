package beinet.cn.googleauthenticatordemo;

import beinet.cn.googleauthenticatordemo.configs.loginValidator.AuthDetails;
import beinet.cn.googleauthenticatordemo.utils.RSAUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 11:52
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String index(AuthDetails authDetails) {
        return this.getClass().getName() + " " + LocalDateTime.now() + " 当前登录:" + authDetails.getAccount();
    }

    private static final String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCsIdWMn0362EYnjmOEdI5TQ+69BhEtRpk9sIxtF3uS5mYOI0mbRDu5yqeW+ueZoGNkysoj5pGJFAnOrx5brepfrT9FOHAz4t7m252xNF4RpjxUsHQrAlvgNiflnV3kB+OvoTwHMmxmRfR945YuLsgdfCeR6nOcOOztZgRWKZBnjFEI67jPnjSOd3yk8cATPsSDLvOMammC4vbrlO58WMDcsVtvy/Rz2gwZpxHmOhedW6UhPvP8ZsEXmIOUTi83s8WOnFlPT6DDqZvkd6SU+bHZpFl10E0HBUBzVf67DvASjwUJ5Fpr5DCogIiv8RcdOx9/vg0il+6fJN8JbQycGSfnAgMBAAECggEAbJ5RDhJ8d5y/8DgWrj+KjBpi7R6F+kcBbf8ZLcHStajzn1o/GV4ePigWRtx/Bt9BuRmKIstc7N4bLjQs+Pcu3T8KnNh3XRZ9R7ovsOF1trdqfJp4Q6eSaFJkLK130Ov1jh81LOlxo7vjAwl4/B2DI6/znE0QtPTq9QcaF3DU8bF8ix+sDazI5YlexUrT+I/QSUwFzcHRf8ULQ9MfPdiD4cY8ii3o4DrCxtQNFbQBIrvrwtEaZu6Hb12EtV0bhf3/VEndIerUNE5IrixCwm6o0fsSCF5+JMMRVAnJZebJoaQ28NVjjAVFpefYbNtNH3RGu4Id5LAVnGLWdmrdCyevGQKBgQDk0h02ERwT0xOQXdgJ5wtkx9bBrvSnv4Ncz3Rcc7VvMnCwU9zl9DCC+HKzqAtXW1GJcMWiWInwhoTlL8egCc61qLh5QAqkkhwrMJwwRWAnhDf8wmW+1K/3qqa8mc8wofE7jRTl6trDZjROmzL2gvLtYrhDszWdaZQYsvMQcBX9SwKBgQDAk/UAPPCoi5ZPZIzZgSbL04JxZ1nxOHjYiKDjiqqzFH79jIuHUfYkAFKe5V0UimKdMsSsD8dOrRzhA3G5Mszq/VmmOy8Il4dORO79yX8jHNN2JxQsO8Wk2gXezvKhxGJuSqxN37/k7DGbRUhVCtAlYy83UG7DmmcdbXccIeFqVQKBgGKOrc5P6EwRqmeQRDFftuSdvr6F4RMvxiD/qhr4K7nNfAQ22vqhBxQidyUyyE/EIYaVcErQ3/kdt7gPsGgaUVKlbW+sSelAlal0spGIEDZ1GLhh8vkTGwgOFOs/RKKp3gHMbqfpVHi9WOJSrszG8mOVFdDzK7C1fPh2e4MlBBOpAoGBAIzhsLJj4orq0yNxo7jays8CBMOBoELf1UhTa6AKqaYosF48mzZ7t13O/qJPmTCVrF7j7Q0S3fOooR/brS6XA2JFxXfehCH8+HdB5rSy29eMFwawwAk8EPLwrKq7KrU9PeUBq9usfG20fPXiUXAlYPB5BumoZd+i7RySIDjscEOhAoGBAIPCTEhQFySqhLatm/YvKGnpWbz89AwBi5obqSMb4UaljUwAmObPgXi3wm1B8uUveBG9l2QK9KyQHn6/2w/Fm6RUiA8c9wDktp6Xewc93bvrocqG4UGLeqV8/W3bHzRroMXDkLWQxyZ1XTfSPsRdWlYm6SvILD+/T6L21fx6SgjA";

    @PostMapping("testRsa")
    public RsaDto rsa(@RequestBody(required = false) RsaDto dto) {
        String decryptData = RSAUtil.decrypt(privateKey, dto.data);
        dto.setData(decryptData);
        dto.setSign(RSAUtil.sign(privateKey, decryptData));
        return dto;
    }

    @Data
    public static class RsaDto {
        private String data;
        private String sign;
    }
}
