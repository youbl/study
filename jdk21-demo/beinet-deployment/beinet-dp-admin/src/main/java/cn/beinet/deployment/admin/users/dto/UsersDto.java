package cn.beinet.deployment.admin.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "用户数据")
public class UsersDto {
    @Schema(description = "用户id，主键")
    private Long id;

    /**
     * 用户名称
     */
    @Size(max = 255)
    @Schema(description = "用户名称")
    private String name;

    /**
     * 用户密码
     */
    @Size(max = 255)
    @Schema(description = "用户密码")
    private String userPassword;

    /**
     * 用户邮箱
     */
    @Size(max = 255)
    @Schema(description = "用户邮箱")
    private String userEmail;

    /**
     * 上次登录时间
     */
    @Schema(description = "上次登录时间")
    private Long lastLoginDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上次登录时间起始，用于条件查询")
    private java.time.LocalDateTime lastLoginDateBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上次登录时间结束，用于条件查询")
    private java.time.LocalDateTime lastLoginDateEnd;

    /**
     * 状态，0：已禁用;1：已启用
     */
    @Schema(description = "状态，0：已禁用;1：已启用")
    private Integer status;

    /**
     * 是否boss，0：非；1：是
     */
    @Schema(description = "是否boss，0：非；1：是")
    private Integer isBoss;

    /**
     * 是否管理员，0：非；1：是
     */
    @Schema(description = "是否管理员，0：非；1：是")
    private Integer isAdmin;

    /**
     * 租户id
     */
    @Schema(description = "租户id")
    private Long tenantId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间起始，用于条件查询")
    private java.time.LocalDateTime createTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间结束，用于条件查询")
    private java.time.LocalDateTime createTimeEnd;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Long updateTime;

    /**
     * 删除标记，0：未删除；1：已删除
     */
    @Schema(description = "删除标记，0：未删除；1：已删除")
    private Integer delflag;

    /**
     * 邀请人id
     */
    @Schema(description = "邀请人id")
    private Long inviteUserId;

    /**
     * 邀请token
     */
    @Size(max = 64)
    @Schema(description = "邀请token")
    private String inviteToken;

    /**
     * 生成mfa的密钥
     */
    @Size(max = 100)
    @Schema(description = "生成mfa的密钥")
    private String mfaKey;

    /**
     * 头像
     */
    @Size(max = 255)
    @Schema(description = "头像")
    private String picture;

    /**
     * 是否测试人员
     */
    @Schema(description = "是否测试人员")
    private Integer isTest;

    /**
     * 备注
     */
    @Size(max = 10)
    @Schema(description = "备注")
    private String memo;

    /**
     * 用户注册ip
     */
    @Size(max = 15)
    @Schema(description = "用户注册ip")
    private String userIp;

    /**
     * 用户注册ip归属地
     */
    @Size(max = 50)
    @Schema(description = "用户注册ip归属地")
    private String userLoc;

}