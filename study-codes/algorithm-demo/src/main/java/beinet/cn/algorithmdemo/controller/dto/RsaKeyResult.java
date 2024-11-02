package beinet.cn.algorithmdemo.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生成的rsa密码: (n,e)是公钥，(n,d)是私钥
 *
 * @author youbl
 * @since 2024/10/25 16:28
 */
@ApiModel(description = "比对结果")
@Data
@Accessors(chain = true)
public class RsaKeyResult {
    @ApiModelProperty(value = "rsa密钥的质数乘积，即(p*q)")
    private Long n;

    @ApiModelProperty(value = "对应n的欧拉函数解，小于n且与n互质的数字个数，即(p-1)(q-1)")
    private Long euler;

    @ApiModelProperty(value = "固定的一个质数值，一般选是65537")
    private Long e;
    @ApiModelProperty(value = "满足线性同余方程的解：d * e ≡ 1 (mod euler)")
    private Long d;

    @ApiModelProperty(value = "rsa密钥的质数之一")
    private Long p;
    @ApiModelProperty(value = "rsa密钥的质数之一")
    private Long q;

    @ApiModelProperty(value = "公钥")
    public String getPublicKey() {
        return n + "," + e;
    }

    @ApiModelProperty(value = "私钥")
    public String getPrivateKey() {
        return n + "," + d;
    }
}
