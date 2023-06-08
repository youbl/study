package beinet.cn.frontstudy.imgs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/8 15:22
 */
@RestController
public class ImgController {
    /**
     * 以base64形式返回图片字符串，可以以json并附带其它数据一起返回。
     * 比如验证码图片，加验证码对应的序号
     *
     * @return map
     * @throws IOException
     */
    @GetMapping("img")
    public Map<String, String> getImg() throws IOException {
        String strForImg = "beinet.cn";

        Map<String, String> ret = new HashMap<>();
        ret.put("img", ImgHelper.getImageBase64(strForImg));
        ret.put("sn", "123");
        ret.put("ts", String.valueOf(System.currentTimeMillis()));
        return ret;
    }
}
