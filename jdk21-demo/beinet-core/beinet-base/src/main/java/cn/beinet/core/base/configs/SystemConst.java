package cn.beinet.core.base.configs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 系统级的常量
 *
 * @author youbl
 * @since 2024/7/18 13:44
 */
@Slf4j
public class SystemConst {

    /**
     * 通过 java -jar 形式启动时的启动目录，就是在哪个目录下启动的命令行。
     * 注：不一定是java.exe目录
     */
    @Getter
    private static String startDir;

    /**
     * 主jar包文件的所在目录
     */
    @Getter
    private static String baseDir;

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
        baseDir = readBaseDir();
        startDir = readStartDir();

        serverIp = readServerIp();
        outerIp = readOuterIp();
        osInfo = readOsInfo();
    }

    private static String readBaseDir() {
        try {
            // 获取 jar 文件所在目录
            String path = SystemConst.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath();
            // 解码 URL 编码的路径
            path = URLDecoder.decode(path, StandardCharsets.UTF_8);

            String jarPath = new File(path)
                    .getAbsolutePath()
                    .replace('\\', '/');

            log.info("=====Start class path is: " + jarPath + "=====");
            jarPath = replaceNestedDir(jarPath);
            log.info("=====Start jar's path is: " + jarPath + "=====");

            // 上面得到的是jar的文件路径，要取它所在的目录
            int idx = jarPath.lastIndexOf('/');
            if (idx > 0) {
                jarPath = jarPath.substring(0, idx);
            }
            if (!jarPath.endsWith("/") && !jarPath.endsWith("\\")) {
                jarPath = jarPath + "/";
            }
            return jarPath;
        } catch (Exception e) {
            log.error("获取jar目录失败:", e);
            return "";
        }
    }

    // 通过java -jar启动时，会得到nested开头 和 .jar/结尾的内容目录，要替换掉
    private static String replaceNestedDir(String path) {
        // D:/mine/nested:/D:/mine/beinet-dp-admin-1.0-SNAPSHOT.jar/!BOOT-INF/lib/beinet-base-1.0-SNAPSHOT.jar!
        String nested = "nested:/";
        int idxNest = path.indexOf(nested);
        if (idxNest != -1) {
            path = path.substring(idxNest + nested.length());
        }
        String jar = ".jar/";
        int idxJar = path.indexOf(jar);
        if (idxJar != -1) {
            // 减1，是去掉 .jar后的那个/
            path = path.substring(0, idxJar + jar.length() - 1);
        }
        return path;
    }

    private static String readStartDir() {
        String dir = System.getProperty("user.dir");
//        if (dir == null) {
//            dir = System.getProperty("user.home");
//        }
        if (dir == null) {
            dir = "/";
        } else if (!dir.endsWith("/") && !dir.endsWith("\\")) {
            dir = dir + "/";
        }
        return dir.replace('\\', '/');
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
