package beinet.cn.controller;

import beinet.cn.utils.GoogleJwtUtils;
import beinet.cn.utils.dto.GoogleUser;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@RestController
@Slf4j
public class TokenController {

    @GetMapping("google/credential")
    @SneakyThrows
    public GoogleUser credential(@RequestParam String credential) {
        return GoogleJwtUtils.getMailFromCredential(credential);
    }

}
