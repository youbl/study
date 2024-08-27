package beinet.cn.awss3demo.demos.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class HttpUtils {

    /**
     * 获取指定url的header列表
     *
     * @param url url
     * @return header列表
     */
    public static String getHeaderByName(String url, String headerName) {
        Map<String, List<String>> headers = getHeaders(url, null);
        if (headers == null || headers.isEmpty())
            return "";
        List<String> values = headers.get(headerName);
        if (values == null || values.isEmpty())
            return "";
        return values.get(0);
    }

    /**
     * 获取指定url的header列表
     *
     * @param url url
     * @return header列表
     */
    public static Map<String, List<String>> getHeaders(String url) {
        return getHeaders(url, null);
    }

    /**
     * 获取指定url的header列表
     *
     * @param url     url
     * @param headers 附加的请求header
     * @return header列表
     */
    public static Map<String, List<String>> getHeaders(String url, Map<String, String> headers) {
        if (!StringUtils.hasLength(url)) {
            throw new RuntimeException("url can't be empty.");
        }
        url = url.trim();
        if (!StringUtils.startsWithIgnoreCase(url, "http://") &&
                !StringUtils.startsWithIgnoreCase(url, "https://")) {
            url = "http://" + url;
        }
        HttpURLConnection connection = null;
        try {
            URL uri = new URL(url);
            connection = (HttpURLConnection) uri.openConnection();
            connection.setRequestMethod("HEAD");
            addRequestHeaders(connection, headers);

            connection.getResponseCode();
            return replaceNullHeader(connection.getHeaderFields());
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static Map<String, List<String>> replaceNullHeader(Map<String, List<String>> headers) {
        Object nullKey = null;
        Map<String, List<String>> ret = new HashMap<>();
        for (String key : headers.keySet()) {
            if (key == nullKey) {
                ret.put("", headers.get(key));
            } else {
                ret.put(key, headers.get(key));
            }
        }
        return ret;
    }

    /**
     * 获取指定url的header里的长度
     *
     * @param url url
     * @return 长度
     */
    public static long getContentLength(String url) {
        try {
            Map<String, List<String>> headers = getHeaders(url);
            for (Map.Entry<String, List<String>> item : headers.entrySet()) {
                if (item.getKey() == null) {
                    // key为null的,value为 [HTTP/1.1 404 Not Found]
                    //if(item.getValue().get(0).indexOf(" 404 ") > 0){
                    //}
                    continue;
                }
                // log.info("{} : {}", item.getKey(), item.getValue());
                if ("Content-Length".equalsIgnoreCase(item.getKey())) {
                    List<String> contentLengths = item.getValue();
                    if (contentLengths == null || contentLengths.isEmpty())
                        return 0;
                    return Long.parseLong(contentLengths.get(0));
                }
            }
            return 0;
        } catch (Exception exp) {
            log.warn("取长度失败:" + exp.getMessage());
            return 0;
        }
    }

    private static void addRequestHeaders(HttpURLConnection connection, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (StringUtils.hasLength(key) && StringUtils.hasLength(val)) {
                connection.setRequestProperty(key, val);
            }
        }
    }
}
