package beinet.cn.controller;

import beinet.cn.utils.GoogleReCAPTCHAUtil;
import beinet.cn.utils.dto.GoogleRecaptchaResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 * @author youbl
 * @since 2025/5/13 17:42
 */
@RestController
public class ReCAPTCHAController {
    @PostMapping("google/reCAPTCHA")
    public GoogleRecaptchaResult reCAPTCHA(@RequestBody String requestBody) {
        Map<String, String> kv = splitKeyVal(requestBody);
        String recaptchaResponse = kv.get("g-recaptcha-response");

        if (recaptchaResponse.isEmpty()) {
            throw new RuntimeException("验证码获取失败，请重试");
        }
        // 去Google验证, 有返回表示验证成功
        return GoogleReCAPTCHAUtil.createAssessment(recaptchaResponse);
    }

    private Map<String, String> splitKeyVal(String requestBody) {
        Map<String, String> ret = new HashMap<>();
        if (requestBody == null) {
            return ret;
        }
        String[] keyValArr = requestBody.split("&");
        for (String item : keyValArr) {
            String[] keyVal = getKeyValArr(item);
            if (keyVal.length != 2) {
                continue;
            }
            String key = keyVal[0].trim();
            String val = keyVal[1].trim();
            if (key.isEmpty() || val.isEmpty()) {
                continue;
            }
            ret.put(key, val);
        }
        return ret;
    }

    private String[] getKeyValArr(String keyVal) {
        int idx = keyVal.indexOf("=");
        // 不存在，或是第1个字符，或是最后1个字符
        if (idx <= 0 || idx == keyVal.length() - 1) {
            return new String[0];
        }
        return new String[]{keyVal.substring(0, idx), keyVal.substring(idx + 1)};
    }
}
