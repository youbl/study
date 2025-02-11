package beinet.cn.utils;

import beinet.cn.utils.dto.GoogleUser;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.util.Collections;

/**
 * 根据Google的Credential获取邮箱、姓名等信息的类
 * @author youbl
 * @since 2024/12/26 16:02
 */
public class GoogleJwtUtils {
    private final static String CLIENT_ID = "1093585410643-qi1pdejjt5urqevtd3o4k0joilg02711.apps.googleusercontent.com";

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
}
