package cn.beinet.sdk.event.constants;

/**
 * 事件相关的常量
 *
 * @author youbl
 * @since 2024/7/12 15:01
 */
public final class EventConst {
    /**
     * 事件体里的事件类型字段
     */
    public final static String REPORT_TYPE = "mainType";
    /**
     * 事件体里的事件子类型字段
     */
    public final static String SUB_TYPE = "subType";
    /**
     * 事件体里的数据信息字段
     */
    public final static String EXT_INFO = "extInfo";

    /**
     * 数据体里的用户id字段名
     */
    public static final String USER_ID = "userId";
    /**
     * 数据体里的租户id字段名
     */
    public static final String TENANT_ID = "tenantId";
    /**
     * 数据体里的设备id字段名
     */
    public static final String MACHINE_ID = "machineId";

    /**
     * OperationLog注解是否记录所有事件
     */
    public static final String ALL = "all";
}
