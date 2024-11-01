package beinet.cn.algorithmdemo.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 * @author youbl
 * @since 2024/10/25 16:28
 */
@ApiModel(description = "比对结果")
@Data
@Accessors(chain = true)
public class EuclidExtResult {

    @ApiModelProperty(value = "ax+by=z的算式")
    private String formula;

    @ApiModelProperty(value = "最大公约数")
    private Long greatCommonDivisor;

    @ApiModelProperty(value = "较大数字的系数，即x")
    private Long bigFactor;

    @ApiModelProperty(value = "较小数字的系数，即y")
    private Long smallFactor;

    @ApiModelProperty(value = "输入的数字中较大的一个")
    private Long bigNum;

    @ApiModelProperty(value = "输入的数字中较小的一个")
    private Long smallNum;
}
