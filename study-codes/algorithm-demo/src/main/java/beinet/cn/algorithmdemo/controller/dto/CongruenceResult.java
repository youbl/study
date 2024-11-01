package beinet.cn.algorithmdemo.controller.dto;

import lombok.Data;

/**
 * 线性同余方程的解
 * @author youbl
 * @since 2024/11/1 17:34
 */
@Data
public class CongruenceResult {
    private String errMsg;
    private Long congruence;

    public boolean isOk() {
        return errMsg == null || congruence != null;
    }

    public static CongruenceResult fail(String errMsg) {
        CongruenceResult result = new CongruenceResult();
        result.errMsg = errMsg;
        return result;
    }

    public static CongruenceResult ok(long congruence) {
        CongruenceResult result = new CongruenceResult();
        result.congruence = congruence;
        return result;
    }
}
