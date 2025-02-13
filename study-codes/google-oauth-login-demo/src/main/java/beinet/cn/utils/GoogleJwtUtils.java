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
import org.springframework.util.Assert;
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
}
