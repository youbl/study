package beinet.cn.demologmybatis.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("projects")
public class Projects extends BaseEntity<Long> {
    /**
     * 项目名
     */
    private String name;

    /**
     * 项目说明
     */
    private String summary;

    /**
     * 项目所属分组
     */
    private String groupName;

    /**
     * 项目所属人
     */
    private String owner;

    /**
     * 项目的git地址
     */
    private String gitUrl;

    /**
     * 对应Jenkins的job名称
     */
    private String jenkinsName;

    /**
     * 项目所属状态
     */
    private int status;
}
