package beinet.cn.controller;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@RestController
public class TokenController {
    @GetMapping("google/token")
    @SneakyThrows
    public GoogleInfoResult token(@RequestParam String token) {
        final RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + URLEncoder.encode(token, "UTF-8");
        GoogleInfoResult result = restTemplate.getForObject(url, GoogleInfoResult.class);
        Assert.notNull(result, "Google token is not valid");
        return result;
    }

    /**
     * 下面接口返回的json数据格式
     * <a href="https://www.googleapis.com/oauth2/v1/userinfo">...</a>
     *
     * @author youbl
     * @since 2024/9/11 10:53
     */
    @Data
    @Accessors(chain = true)
    public static class GoogleInfoResult {
        private String id;
        private String email;
        private Boolean verified_email;
        private String name;
        private String given_name;
        private String family_name;
        private String picture;
    }

}
