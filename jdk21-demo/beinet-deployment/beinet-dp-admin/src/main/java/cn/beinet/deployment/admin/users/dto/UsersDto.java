package cn.beinet.deployment.admin.users.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

/**
 *
 * @author youbl.blog.csdn.net
 * @since 2024-11-19 12:28:00
 */
@Data
@Accessors(chain = true)
public class UsersDto {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名称
     */
    @Size(max = 255)
    private String name;

    /**
     * 用户密码
     */
    @Size(max = 255)
    private String userPassword;

    /**
     * 用户邮箱
     */
    @Size(max = 255)
    private String userEmail;

    /**
     * 上次登录时间
     */
    private Long lastLoginDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime lastLoginDateBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime lastLoginDateEnd;

    /**
     * 状态，0：已禁用;1：已启用
     */
    private Integer status;

    /**
     * 是否boss，0：非；1：是
     */
    private Integer isBoss;

    /**
     * 是否管理员，0：非；1：是
     */
    private Integer isAdmin;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    private Long createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime createTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime createTimeEnd;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 删除标记，0：未删除；1：已删除
     */
    private Integer delflag;

    /**
     * 邀请人id
     */
    private Long inviteUserId;

    /**
     * 邀请token
     */
    @Size(max = 64)
    private String inviteToken;

    /**
     * 生成mfa的密钥
     */
    @Size(max = 100)
    private String mfaKey;

    /**
     * 头像
     */
    @Size(max = 255)
    private String picture;

    /**
     * 是否测试人员
     */
    private Integer isTest;

    /**
     * 备注
     */
    @Size(max = 10)
    private String memo;

    /**
     * 用户注册ip
     */
    @Size(max = 15)
    private String userIp;

    /**
     * 用户注册ip归属地
     */
    @Size(max = 50)
    private String userLoc;


}