package cn.beinet.core.base.consts;

import org.springframework.http.MediaType;

/**
 * 请求头常量
 *
 * @author youbl
 * @since 2024/7/17 19:15
 */
public class HeaderConst {
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;// "application/json";

    public final static String USER_AGENT = "User-Agent";
    public final static String USER_AGENT_DEFAULT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36";

    public final static String AUTH = "authorization";
    public final static String MACHINE_ID = "x-machine-id";
    public final static String USER_ID = "x-user-id";
    public final static String TENANT_ID = "x-tenant-id";
    public final static String OS = "x-os";
    public final static String VERSION = "x-version";
    public final static String STARTUP_TIME = "x-startup-time";
    public final static String TIME_ZONE = "x-time-zone";
    public final static String PRODUCT = "x-product";
}
