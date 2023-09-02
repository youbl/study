package beinet.cn.googleauthenticatordemo.configs.loginAction;

import beinet.cn.googleauthenticatordemo.utils.ImgCodeUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码服务类
 *
 * @author youbl
 * @since 2023/5/16 17:45
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ImgCodeService {
    private final ImgCodeUtil codeUtil;

    private Map<String, String> codeSnMap = new ConcurrentHashMap<>();

    /**
     * 获取图形验证码并返回
     *
     * @return
     */
    public ImgCodeDto getImgCode() {
        String code = codeUtil.getRndTxt(4);
        // 这里生成sn，并把sn和code的关联关系存入redis或数据库
        // todo: 如果未使用过的code，需要在10分钟内过期，这里没做
        String sn = getSn(code);

        String base64 = codeUtil.getImageBase64(code);
        return new ImgCodeDto()
                .setCodeSn(sn)
                .setCodeBase64(base64);
    }

    private String getSn(String code) {
        int tryTime = 10;

        String sn = codeUtil.getRndNum(6);
        // putIfAbsent 不存在时加入kv，并返回null；存在时不加入，并返回现有的值
        // 这个while确保找到一个未使用的code，避免覆盖以前的值
        while (codeSnMap.putIfAbsent(sn, code) != null && tryTime > 0) {
            sn = codeUtil.getRndNum(6);
            // 避免程序bug，导致死循环，最多重试10次
            tryTime--;
        }
        Assert.isTrue(tryTime > 0, "获取sn连续10次重复");
        return sn;
    }

    public boolean validImgCode(String sn, String code) {
        Assert.isTrue(StringUtils.hasLength(code) && StringUtils.hasLength(sn),
                "输入的sn或code为空");

        // 删除sn与code的映射关系，不让重复使用
        String value = codeSnMap.remove(sn);
        return (code.equalsIgnoreCase(value));
    }

    @Data
    @Accessors(chain = true)
    public static class ImgCodeDto {
        private String codeBase64;
        private String codeSn;
    }
}
