package cn.beinet.core.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ipinfo接口返回的数据
 * <a href="https://ipinfo.io/1.1.1.1/json">...</a>
 * @author youbl
 * @since 2024/10/30 11:37
 */
@Data
@Accessors(chain = true)
public class IpInfoDto {
    private String ip;
    private String hostname;
    private String anycast;
    private String city;
    private String region;
    private String country;
    private String loc;
    private String org;
    private String postal;
    private String timezone;
    private String readme;
    private Boolean bogon;
}
