package cn.beinet.core.base.configs;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * 系统级的常量
 *
 * @author youbl
 * @since 2024/7/18 13:44
 */
public class SystemConst {
    /**
     * 服务器IP
     */
    @Getter
    private static String serverIp;
    /**
     * 公网出口IP
     */
    @Getter
    private static String outerIp;
    /**
     * 系统信息
     */
    @Getter
    private static String osInfo;

    static {
        refresh();
    }

    /**
     * 重新读取这些系统常量
     */
    public static void refresh() {
        serverIp = readServerIp();
        outerIp = readOuterIp();
        osInfo = readOsInfo();
    }

    private static String readServerIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static String readOsInfo() {
        try {
            return System.getProperty("os.name") + "," + System.getProperty("os.version") + "," + System.getProperty("os.arch");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private static String readOuterIp() {
        String ipUrl = "https://checkip.amazonaws.com/"; // 获取出口IP的url
        HttpURLConnection conn = null;
        try {
            //URI.create(ipUrl).toURL().openConnection();
            conn = (HttpURLConnection) (new URL(ipUrl).openConnection());
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return parseIP(response.toString());
                }
            }
            return ("err-status:" + responseCode);
        } catch (Exception e) {
            return "err:" + e.getMessage();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
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
