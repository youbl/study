package cn.beinet.core.utils;

import cn.beinet.core.utils.dto.IpApiDto;
import cn.beinet.core.utils.dto.IpInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author : youbl
 * @since 2021/9/15 20:57
 */
@Slf4j
public class IpHelper {

    /**
     * 获取当前服务器的本地IP
     * @return 本地ip
     */
    public static String getServerIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return e.getMessage();
        }
    }

    /**
     * 返回请求上下文中所有的ip，并拼接为字符串返回。
     * 注意：改此方法，同时要修改下面的方法：parseFromGetClientIp
     *
     * @param request 请求上下文
     * @return ip串，例如：8.217.76.130;f:8.217.76.130;r:8.217.76.130
     */
    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasLength(forwarded)) {
            remoteAddr += ";f:" + forwarded;
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasLength(realIp)) {
            remoteAddr += ";r:" + realIp;
        }
        String cdnIp = request.getHeader("ali-cdn-real-ip");
        if (StringUtils.hasLength(cdnIp)) {
            remoteAddr += ";d:" + cdnIp;
        }
        return remoteAddr;
    }

    /**
     * 把上面的getClientIp，进行反向处理和返回
     * @param ipStr getClientIp返回的结果
     * @return map
     */
    public static Map<String, String> parseFromGetClientIp(String ipStr) {
        Map<String, String> ret = new HashMap<String, String>();
        String[] arr = ipStr.split(";");
        for (String str : arr) {
            int idx = str.indexOf(":");
            if (idx < 0) {
                // request.getRemoteAddr()
                ret.put("", str.trim());
                continue;
            }
            String type = str.substring(0, idx);
            String ip = str.substring(idx + 1).trim();
            switch (type) {
                case "f":
                    ret.put("x-forwarded-for", ip);
                    break;
                case "r":
                    ret.put("x-real-ip", ip);
                    break;
                case "d":
                    ret.put("ali-cdn-real-ip", ip);
                    break;
            }
        }
        return ret;
    }

    /**
     * 获取后端的出口IP
     *
     * @param ipUrl 检测出口IP的地址，可空
     * @return 出口IP
     */
    public static String getOuterIp(String ipUrl) {
        if (!StringUtils.hasLength(ipUrl)) {
            ipUrl = "https://checkip.amazonaws.com/";
        }
        String ret = getResponse(ipUrl);
        return parseIP(ret);
    }

    /**
     * 获取发起放的请求ip地址
     * 注意：因为客户端Header可以伪造，不推荐此方法用于对安全有要求的场景。
     *
     * @param request 请求上下文
     * @return IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        try {
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("ali-cdn-real-ip");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-real-ip");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception exp) {
            log.error("getIPErr", exp);
        }
        if (ip == null) {
            logRequestHeader(request);
            return "";
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip.split(",")[0].trim();
    }

    public static String getRequestHeader(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURL())
                .append("\r\n\n");
        sb.append("IP-addr:")
                .append(request.getRemoteAddr())
                .append(" Port:")
                .append(request.getRemotePort())
                .append("\r\n\nAll Headers:\r\n");

        Enumeration<String> allHeader = request.getHeaderNames();
        while (allHeader.hasMoreElements()) {
            String header = allHeader.nextElement();
            Enumeration<String> vals = request.getHeaders(header);
            while (vals.hasMoreElements()) {
                String val = vals.nextElement();
                sb.append(header)
                        .append(" : ")
                        .append(val)
                        .append("\r\n");
            }
        }
        return sb.toString();
    }

    private static void logRequestHeader(HttpServletRequest request) {
        try {
            String headers = getRequestHeader(request);
            log.error("getIPErr empty: " + headers + " callStack:" + Arrays.toString(Thread.currentThread().getStackTrace()));
//            if (Objects.isNull(request.getMethod())) {
//                throw new RuntimeException("未知请求，抛个异常打印堆栈来看看。");
//            }
        } catch (Exception exp) {
            log.error("logHeaderErr", exp);
        }
    }

    /**
     * 获取发起放的请求ip地址
     * 注意：因为客户端Header可以伪造，不推荐此方法用于对安全有要求的场景。
     *
     * @param request 请求上下文
     * @return IP地址
     */
    public static String getIpAddr(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("ali-cdn-real-ip");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("x-real-ip");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip.split(",")[0].trim();
    }

    /**
     * 把IP或域名，转换为IP对象返回
     *
     * @param ipOrDomain IP或域名
     * @return IP对象或null
     */
    public static InetAddress toAddress(String ipOrDomain) {
        try {
            return InetAddress.getByName(ipOrDomain);
        } catch (Exception exp) {
            return null;
        }
    }


//    /**
//     * 判断给出的IP是否IPv6
//     *
//     * @param ip ip
//     * @return true false
//     */
//    public static boolean isIPv6(String ip) {
//        try {
//            Inet6Address inetAddress = (Inet6Address) Inet6Address.getByAddress(ip.getBytes());
//            return inetAddress != null;
//        } catch (UnknownHostException | ClassCastException e) {
//            return false;
//        }
//    }

    /**
     * 判断给出的IP是否IPv4
     *
     * @param ip ip
     * @return true false
     */
    public static boolean isIPv4(String ip) {
        try {
            String[] arr = ip.split("\\.");
            if (arr.length != 4)
                return false;

            // 不能用Byte.parseByte，因为Byte范围只支持 -128~127
            byte[] arrBt = new byte[]{(byte) Integer.parseInt(arr[0]),
                    (byte) Integer.parseInt(arr[1]),
                    (byte) Integer.parseInt(arr[2]),
                    (byte) Integer.parseInt(arr[3])};
            Inet4Address inetAddress = (Inet4Address) Inet4Address.getByAddress(arrBt);
            return inetAddress != null;
        } catch (UnknownHostException | ClassCastException e) {
            return false;
        }
    }

    /**
     * 把ip地址转换为整数返回
     *
     * @param ip ip地址
     * @return 整数
     */
    public static long ipaddrToNumber(String ip) {
        String[] ipArr = ip.split("\\.");
        if (ipArr.length != 4) {
            throw new IllegalArgumentException("IP地址格式不对，应该有3个小数点");
        }
        long ret = 0;
        // 验证每个项是否小于等于255
        for (int i = 0; i < 4; i++) {
            int number = Integer.parseInt(ipArr[i]);
            if (number > 255 || number < 0) {
                throw new IllegalArgumentException("IP地址中的每个项都应在0~255之间");
            }
            ret = (ret << 8) + number;
        }
        // 转无符号数，避免负数返回
        return ret;
    }

    /**
     * 给定的ip，是否在给定的cidr子网范围内
     * 注：本方法没判断起始IP是否是合法的子网起始ip
     *
     * @param ip ip
     * @param startIP  子网起始ip
     * @param ipMask 子网掩码
     * @return 是否在范围内
     */
    public static boolean inIpAddrCIDR(String ip, String startIP, int ipMask) {
        if (ipMask < 1 || ipMask > 32)
            throw new Error("子网掩码应在1~32之间");
        long ipNum = ipaddrToNumber(ip);
        long startIPNum = ipaddrToNumber(startIP);
        long endIPNum = (long) (startIPNum + (Math.pow(2, 32 - ipMask) - 1));
        return ipNum >= startIPNum && ipNum <= endIPNum;
    }

    /**
     * 给定的ip，是否在给定的ip起止范围内
     *
     * @param ip 要判断的ip
     * @param startIP ip范围起始值
     * @param endIp ip范围结束值
     * @return 是否在范围内
     */
    public static boolean inIpAddrRange(String ip, String startIP, String endIp) {
        long ipNum = ipaddrToNumber(ip);
        long startIPNum = ipaddrToNumber(startIP);
        long endIPNum = ipaddrToNumber(endIp);
        return ipNum >= startIPNum && ipNum <= endIPNum;
    }

    /**
     * 先尝试通过ipinfo获取归属地，失败时，再获取ip-api
     * @param ip ip
     * @return 归属地
     */
    public static String getLocation(String ip) {
        String location = "-";
        try {
            var ipInfoDto = getLocationFromIpInfo(ip);
            location = ipInfoDto.getCountry();
            if (StringUtils.hasLength(ipInfoDto.getCity())) {
                location += ":" + ipInfoDto.getCity();
            } else if (StringUtils.hasLength(ipInfoDto.getRegion())) {
                location += ":" + ipInfoDto.getRegion();
            }
        } catch (Exception e1) {
            log.error("getLocationFromIpInfoErr: {}", ip, e1);

            try {
                var ipApiDto = getLocationFromIpApi(ip);
                location = ipApiDto.getCountryCode();
                if (StringUtils.hasLength(ipApiDto.getCity())) {
                    location += ":" + ipApiDto.getCity();
                } else if (StringUtils.hasLength(ipApiDto.getRegionName())) {
                    location += ":" + ipApiDto.getRegionName();
                } else if (StringUtils.hasLength(ipApiDto.getRegion())) {
                    location += ":" + ipApiDto.getRegion();
                }
            } catch (Exception e2) {
                log.error("getLocationFromIpApiErr: {}", ip, e2);
            }
        }
        return location;
    }

    /**
     * 从ip-api获取IP的归属地信息
     *
     * @param ip IP地址
     * @return 归属地信息
     */
    public static IpApiDto getLocationFromIpApi(String ip) {
        if (isPrivateIpAddr(ip)) {
            return new IpApiDto()
                    .setQuery(ip)
                    .setCountry("private ip")
                    .setStatus("fail");
        }
        // 返回值参考：
        // {"status":"success","country":"United States","countryCode":"US","region":"MO","regionName":"Missouri","city":"Kansas City","zip":"64184","lat":39.1027,"lon":-94.5778,"timezone":"America/Chicago","isp":"AT\u0026T Services, Inc.","org":"CFWN Pool ATTCT-NMPL23","as":"AS7018 AT\u0026T Services, Inc.","query":"12.88.210.103"}
        // {"status":"success","country":"China","countryCode":"CN","region":"XJ","regionName":"Xinjiang","city":"Ürümqi","zip":"830000","lat":43.8256,"lon":87.6169,"timezone":"Asia/Urumqi","isp":"CNC Group CHINA169 Xinjiang Province Network","org":"","as":"AS4837 CHINA UNICOM China169 Backbone","query":"124.88.210.103"}
        String apiUrl = "http://ip-api.com/json/" + ip;
        String result = getResponse(apiUrl);
        return JsonHelper.toBean(result, IpApiDto.class);
    }

    /**
     * 从ip-info获取IP的归属地信息
     *
     * @param ip IP地址
     * @return 归属地信息
     */
    public static IpInfoDto getLocationFromIpInfo(String ip) {
        if (isPrivateIpAddr(ip)) {
            return new IpInfoDto()
                    .setIp(ip)
                    .setCountry("private ip")
                    .setBogon(true);
        }
        // 返回值参考：
        // {"ip":"124.88.210.103","city":"Shanghai","region":"Shanghai","country":"CN","loc":"31.2222,121.4581","org":"AS4837 CHINA UNICOM China169 Backbone","postal":"200000","timezone":"Asia/Shanghai","readme":"https://ipinfo.io/missingauth"}
        // {"ip":"121.88.210.103","city":"Seoul","region":"Seoul","country":"KR","loc":"37.5660,126.9784","org":"AS10036 DLIVE","postal":"03141","timezone":"Asia/Seoul","readme":"https://ipinfo.io/missingauth"}
        String apiUrl = "https://ipinfo.io/" + ip + "/json";
        String result = getResponse(apiUrl);
        return JsonHelper.toBean(result, IpInfoDto.class);
    }

    private static String getResponse(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) (new URL(url).openConnection());
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000); // 设置连接超时为3秒
            conn.setReadTimeout(3000); // 设置读取超时为3秒
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }
            }
            return ("响应：" + responseCode);
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    /**
     * 判断给定的ip，是否属于 IANA定义的保留地址（即私有地址）
     *
     * @param ip 给定的ip
     * @return 是否私有地址
     */
    public static boolean isPrivateIpAddr(String ip) {
        String[][] privateIp = {
                {"0.0.0.0", "0.255.255.255"},  // 0.0.0.0/8
                {"10.0.0.0", "10.255.255.255"},  // 10.0.0.0/8
                {"100.64.0.0", "100.127.255.255"},  // 100.64.0.0/10
                {"127.0.0.0", "127.255.255.255"},  // 127.0.0.0/8
                {"169.254.0.0", "169.254.255.255"},  // 169.254.0.0/16
                {"172.16.0.0", "172.31.255.255"},  // 172.16.0.0/12
                {"192.0.0.0", "192.0.0.255"},  // 192.0.0.0/24
                {"192.0.2.0", "192.0.2.255"},  // 192.0.2.0/24
                {"192.88.99.0", "192.88.99.255"},  // 192.88.99.0/24
                {"192.168.0.0", "192.168.255.255"},  // 192.168.0.0/16
                {"198.18.0.0", "198.19.255.255"},  // 198.18.0.0/15
                {"198.51.100.0", "198.51.100.255"},  // 198.51.100.0/24
                {"203.0.113.0", "203.0.113.255"},  // 203.0.113.0/24
                {"224.0.0.0", "239.255.255.255"},  // 224.0.0.0/4
                {"233.252.0.0", "233.252.0.255"},  // 233.252.0.0/24
                {"240.0.0.0", "255.255.255.254"},  // 240.0.0.0/4
                {"255.255.255.255", "255.255.255.254"}  // 255.255.255.255/32
        };
        for (String[] range : privateIp) {
            if (inIpAddrRange(ip, range[0], range[1])) {
                return true;
            }
        }
        return false;
    }

    private static String parseIP(String str) {
        // 使用正则表达式提取IP地址
        String ipPattern = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(ipPattern);
        java.util.regex.Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return str;
        }
    }
}
