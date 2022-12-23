package beinet.cn.demologmybatis.dal.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity<KeyType extends Serializable> implements Serializable {
    @TableId(type = IdType.AUTO)
    private KeyType id;

    /**
     * 插入时间，由数据库填充，定义：
     * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
     */
    @TableField(
            value = "createTime",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER
            //fill = FieldFill.INSERT
    )
    private LocalDateTime createTime;

    /**
     * 最后更新时间，由数据库填充，定义：
     * `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
     */
    @TableField(
            value = "updateTime",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER
            //fill = FieldFill.INSERT_UPDATE
    )
    private LocalDateTime updateTime;

}
