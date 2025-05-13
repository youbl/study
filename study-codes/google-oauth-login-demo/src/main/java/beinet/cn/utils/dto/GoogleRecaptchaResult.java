package beinet.cn.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 谷歌人机校验结果
 * @author youbl
 * @since 2025/5/13 20:52
 */
@Data
@Accessors(chain = true)
public class GoogleRecaptchaResult {
    /**
     * 得分情况
     */
    private String score;
    /**
     * 得分原因
     */
    private String reason = "";
    private String assessmentName;
}
