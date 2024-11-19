package cn.beinet.core.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ipapi接口返回的数据
 * <a href="http://ip-api.com/json/1.1.1.1">...</a>
 * @author youbl
 * @since 2024/10/30 11:37
 */
@Data
@Accessors(chain = true)
public class IpApiDto {
    private String status;
    private String query;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String lat;
    private String lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
}
