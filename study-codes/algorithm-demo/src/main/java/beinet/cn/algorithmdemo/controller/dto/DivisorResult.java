package beinet.cn.algorithmdemo.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 新类
 * @author youbl
 * @since 2024/10/25 16:28
 */
@ApiModel(description = "比对结果")
@Data
@Accessors(chain = true)
public class DivisorResult {
    @ApiModelProperty(value = "原数=约数的乘积算式")
    private String mul;
    @ApiModelProperty(value = "属于质数的约数列表")
    private Set<Long> prime;
}
