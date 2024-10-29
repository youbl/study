package beinet.cn.algorithmdemo.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 * @author youbl
 * @since 2024/10/25 15:08
 */
@ApiModel(description = "比对结果")
@Data
@Accessors(chain = true)
public class CompareResult {
    @ApiModelProperty(value = "准确性")
    private boolean result;

    @ApiModelProperty(value = "方法1耗时")
    private long costMillis1;

    @ApiModelProperty(value = "方法2耗时")
    private long costMillis2;

}
