package cn.beinet.core.web.context;

public class ContextConstants {
    public static final String HEADER_PREFIX = "x-";
    public static final String HEADER_PREFIX_ACCEPT = "accept-";
    public static final String HEADER_MEMBER_ID = "x-member-id";
    public static final String HEADER_MEMBER_ROLE = "x-member-role";
    public static final String HEADER_MEMBER_NICKNAME = "x-member-nickname";
    public static final String HEADER_IS_BOSS = "x-is-boss";
    public static final String HEADER_IS_ADMIN = "x-is-admin";

    public static final String HEADER_IS_DEVOPS = "x-is-devops";
    public static final String HEADER_USER_ID = "x-user-id";
    public static final String HEADER_USER_EMAIL = "x-user-email";
    public static final String HEADER_TENANT_ID = "x-tenant-id";
    public static final String HEADER_X_TRACE_ID = "x-trace-id";
    public static final String HEADER_MACHINE_ID = "x-machine-id";
    public static final String HEADER_MACHINE_MAC = "x-machine-mac";
    public static final String HEADER_MACHINE_NAME = "x-machine-name";
    public static final String HEADER_VERSION = "x-version";
    public static final String HEADER_ENCRYPT = "x-encrypt";
    public static final String HEADER_OS = "x-os";
    public static final String HEADER_SOURCE = "x-source";
    public static final String HEADER_LANGUAGE = "x-language";
    // 客户端启动时间，时间戳
    public static final String HEADER_STARTUP_TIME = "x-startup-time";
    public static final String HEADER_APPLICATION = "x-application-name";
    public static final String HEADER_PRODUCT = "product";
    public static final String HEADER_ACCEPT_LANGUAGE = "accept-language";
    public static final String HEADER_API_ID = "x-api-id";
    public static final String HEADER_NONCE_ID = "x-nonce-id";
    public static final String HEADER_IP = "x-real-ip";
    /**
     * 收到请求的时间，可以便于后端计算响应时长
     */
    public static final String HEADER_REQUEST_TIME = "x-request-time";

    public static final String HEADER_TIME_ZONE = "x-time-zone";

    /**
     * Backend-for-Frontend标识，只会保留在例如app,api等服务
     */
    public static final String HEADER_BFF = "x-backend-for-frontend";
}
