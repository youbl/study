package cn.beinet.core.base.commonDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询基类
 *
 * @author youbl
 * @since 2024/7/18 16:13
 */
@Data
@Schema(description = "分页查询基类")
public class PageDTO {
    /**
     * 每页显示条数，默认 10
     */
    @Schema(description = "每页显示条数，默认 10")
    protected Integer pageSize = 10;

    /**
     * 要获取的页号，从1开始
     */
    @Schema(description = "要获取的页号，从1开始")
    protected Integer pageNo = 1;

    /**
     * 每页显示条数，默认 10
     */
    public Integer getSize() {
        if (pageSize == null || pageSize <= 0) {
            this.pageSize = 10;
        } else if (pageSize > 10000) {
            this.pageSize = 10;
        }
        return pageSize;
    }

    /**
     * 要获取的页号，从1开始
     */
    public Integer getPageNo() {
        if (pageNo == null || pageNo <= 0) {
            this.pageNo = 1;
        }
        return pageNo;
    }

    /**
     * 根据总记录数，返回总页数
     *
     * @param totalNum 总记录数
     * @return 总页数
     */
    public int getPageTotal(int totalNum) {
        int realSize = getSize();
        int ret = totalNum / realSize;
        if (totalNum % realSize > 0)
            ret++;
        return ret;
    }

    /**
     * 根据当前页号 及 分页大小，返回当前页的起始记录序号
     *
     * @return 起始序号
     */
    public int getStart() {
        if (pageNo != null && pageSize != null) {
            return (pageNo - 1) * pageSize;
        }
        return 0;
    }
}
