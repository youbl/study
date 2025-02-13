package beinet.cn.controller;

import beinet.cn.utils.GoogleJwtUtils;
import beinet.cn.utils.dto.GoogleUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TokenController {

    @GetMapping("google/credential")
    @SneakyThrows
    public GoogleUser credential(@RequestParam String credential) {
        return GoogleJwtUtils.getMailFromCredential(credential);
    }

    @GetMapping("google/token")
    @SneakyThrows
    public GoogleUser token(@RequestParam String accessToken) {
        return GoogleJwtUtils.getUserInfoByToken(accessToken);
    }
}
