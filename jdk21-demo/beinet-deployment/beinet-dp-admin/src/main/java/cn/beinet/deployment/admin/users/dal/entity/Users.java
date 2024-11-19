package cn.beinet.deployment.admin.users.dal.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author youbl.blog.csdn.net
 * @since 2024-11-19 12:28:00
 */
@Data
@Accessors(chain = true)
@TableName("users")
public class Users implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    @Size(max = 255)
    @TableField(value = "`name`")
    private String name;

    /**
     * 用户密码
     */
    @Size(max = 255)
    @TableField(value = "`userPassword`")
    private String userPassword;

    /**
     * 用户邮箱
     */
    @Size(max = 255)
    @TableField(value = "`userEmail`")
    private String userEmail;

    /**
     * 上次登录时间
     */
    @TableField(value = "`lastLoginDate`")
    private java.time.LocalDateTime lastLoginDate;

    /**
     * 状态，0：已禁用;1：已启用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 是否boss，0：非；1：是
     */
    @TableField(value = "`isBoss`")
    private Integer isBoss;

    /**
     * 是否管理员，0：非；1：是
     */
    @TableField(value = "`isAdmin`")
    private Integer isAdmin;

    /**
     * 租户id
     */
    @TableField(value = "`tenantId`")
    private Long tenantId;

    /**
     * 创建时间
     */
    @TableField(value = "`createTime`", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private java.time.LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "`updateTime`", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private java.time.LocalDateTime updateTime;

    /**
     * 删除标记，0：未删除；1：已删除
     */
    @TableField(value = "`delflag`")
    private Integer delflag;

    /**
     * 邀请人id
     */
    @TableField(value = "`inviteUserId`")
    private Long inviteUserId;

    /**
     * 邀请token
     */
    @Size(max = 64)
    @TableField(value = "`inviteToken`")
    private String inviteToken;

    /**
     * 生成mfa的密钥
     */
    @Size(max = 100)
    @TableField(value = "`mfaKey`")
    private String mfaKey;

    /**
     * 头像
     */
    @Size(max = 255)
    @TableField(value = "`picture`")
    private String picture;

    /**
     * 是否测试人员
     */
    @TableField(value = "`isTest`")
    private Integer isTest;

    /**
     * 备注
     */
    @Size(max = 10)
    @TableField(value = "`memo`")
    private String memo;

    /**
     * 用户注册ip
     */
    @Size(max = 15)
    @TableField(value = "`userIp`")
    private String userIp;

    /**
     * 用户注册ip归属地
     */
    @Size(max = 50)
    @TableField(value = "`userLoc`")
    private String userLoc;


}
/*
INSERT INTO users (
  name, userPassword, userEmail, lastLoginDate, status, isBoss, isAdmin, tenantId, delflag, inviteUserId, inviteToken, mfaKey, picture, isTest, memo, userIp, userLoc
)VALUES(
  :name, :userPassword, :userEmail, :lastLoginDate, :status, :isBoss, :isAdmin, :tenantId, :delflag, :inviteUserId, :inviteToken, :mfaKey, :picture, :isTest, :memo, :userIp, :userLoc
);

UPDATE users SET
  name = :name, userPassword = :userPassword, userEmail = :userEmail, lastLoginDate = :lastLoginDate, status = :status, isBoss = :isBoss, isAdmin = :isAdmin, tenantId = :tenantId, delflag = :delflag, inviteUserId = :inviteUserId, inviteToken = :inviteToken, mfaKey = :mfaKey, picture = :picture, isTest = :isTest, memo = :memo, userIp = :userIp, userLoc = :userLoc
WHERE ;
*/