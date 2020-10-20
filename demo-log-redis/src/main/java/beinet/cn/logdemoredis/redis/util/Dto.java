package beinet.cn.logdemoredis.redis.util;

import lombok.Data;

@Data
public class Dto {

    /**
     * 调用类名
     */
    private String clas;
    /**
     * 调用方法名
     */
    private String method;
    /**
     * 调用的参数
     */
    private String para;
    /**
     * 方法返回结果
     */
    private Object result;
    /**
     * 执行时长，毫秒
     */
    private long costTime;
    /**
     * 备注
     */
    private String remark;

    /**
     * 出现的异常
     */
    private Exception exp;
}