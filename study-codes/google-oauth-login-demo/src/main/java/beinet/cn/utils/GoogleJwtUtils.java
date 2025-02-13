package beinet.cn.utils;

import beinet.cn.utils.dto.GoogleUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 根据Google的Credential获取邮箱、姓名等信息的类
 * @author youbl
 * @since 2024/12/26 16:02
 */
public class GoogleJwtUtils {
    private final static String CLIENT_ID = "1093585410643-qi1pdejjt5urqevtd3o4k0joilg02711.apps.googleusercontent.com";
    private final static String CLIENT_SCREPT = "GOOGLE后台查找，跟ClientID一起的那个Secret";

    private final static RestTemplate restTemplate = new RestTemplate();

    @SneakyThrows
    public static GoogleUser getMailFromCredential(String credential) {
        // 官方文档没写Builder的2个参数怎么来的，参考这里写的：https://stackoverflow.com/questions/37172082/android-what-is-transport-and-jsonfactory-in-googleidtokenverifier-builder
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(httpTransport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        GoogleIdToken idToken = verifier.verify(credential);
        Assert.notNull(idToken, "Google credential is not valid");
        GoogleIdToken.Payload payload = idToken.getPayload();
        return convertToGoogleUser(payload);
    }


    /**
     * 根据google返回的access_token，去google api获取完整用户信息
     * @param googleAccessToken google的access_token
     * @return 用户信息
     */
    @SneakyThrows
    public static GoogleUser getUserInfoByToken(String googleAccessToken) {
        Assert.isTrue(StringUtils.hasLength(googleAccessToken), "Google token must not be empty");

        String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" +
                URLEncoder.encode(googleAccessToken, StandardCharsets.UTF_8.displayName());
        GoogleInfoResult result = restTemplate.getForObject(url, GoogleInfoResult.class);
        Assert.notNull(result, "Google token is not valid");
        return convertToGoogleUser(result);
    }


    /**
     * 根据google返回的code，和获取code时使用的callbackUrl，去获取accessToken
     * 参考： <a href="https://developers.google.com/identity/protocols/oauth2/javascript-implicit-flow?hl=zh-cn#handlingresponse">...</a>
     * 测试用这个url获取code
     * <a href="https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A//www.googleapis.com/auth/userinfo.email&access_type=offline&include_granted_scopes=true&response_type=code&state=aaa&redirect_uri=https%3A//cb-gw-pre.zibird.com/app/google/callback&client_id=1093585410643-qi1pdejjt5urqevtd3o4k0joilg02711.apps.googleusercontent.com">...</a>
     * @param code code
     * @param callbackUrl 获取code时使用的callbackUrl，必须匹配，否则会出错
     * @return accessToken
     */
    public static String getAccessToken(String code, String callbackUrl) {
        // 设置请求参数
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("code", code);
        requestParams.add("client_id", CLIENT_ID);
        requestParams.add("client_secret", CLIENT_SCREPT);
        requestParams.add("redirect_uri", callbackUrl);
        requestParams.add("grant_type", "authorization_code");

        // 获取token的url
        String tokenUrl = "https://oauth2.googleapis.com/token";
        // 发送POST请求
        ResponseEntity<OAuth2Token> response = restTemplate.postForEntity(tokenUrl, requestParams, OAuth2Token.class);

        // 返回响应体
        if (response.getBody() != null) {
            return response.getBody().getAccess_token();
        }
        return null;
    }

    private static GoogleUser convertToGoogleUser(GoogleIdToken.Payload payload) {
        if (payload == null) {
            return null;
        }
        return new GoogleUser()
                .setId(payload.getSubject())
                .setEmail(payload.getEmail())
                .setFamilyName(payload.get("family_name") + "")
                .setGivenName(payload.get("given_name") + "")
                .setName(payload.get("name") + "")
                .setPicture(payload.get("picture") + "")
                .setVerifiedEmail((Boolean) payload.get("email_verified"));
    }


    private static GoogleUser convertToGoogleUser(GoogleInfoResult payload) {
        if (payload == null) {
            return null;
        }
        return new GoogleUser()
                .setId(payload.getId())
                .setEmail(payload.getEmail())
                .setFamilyName(payload.getFamily_name())
                .setGivenName(payload.getGiven_name())
                .setName(payload.getName())
                .setPicture(payload.getPicture())
                .setVerifiedEmail(payload.getVerified_email());
    }

    @Data
    @Accessors(chain = true)
    static class GoogleInfoResult {
        private String id;
        private String email;
        private Boolean verified_email;
        private String name;
        private String given_name;
        private String family_name;
        private String picture;
    }

    @Data
    static class OAuth2Token {
        private String access_token;
        private Long expires_in;
        private String scope;
        private String token_type;
        private String id_token;
    }
}
