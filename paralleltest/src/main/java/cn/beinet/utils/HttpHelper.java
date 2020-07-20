package cn.beinet.utils;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class HttpHelper {
    private static String USER_AGENT = "Beinet Client 1.0";
    private static Config _defaultConfig;
    private static HashMap<String, String> _defaultHeader;

    // 收集每次响应的set-cookie
    private static Map<String, HttpCookie> _cookies = new HashMap<>();

    private HttpHelper() {

    }

    static {
        _defaultConfig = InitConfig();

        _defaultHeader = new HashMap<>();
        _defaultHeader.put("Cache-Control", "no-cache");
        _defaultHeader.put("User-Agent", USER_AGENT);
        _defaultHeader.put("Accept-Encoding", "gzip");

        // 全局关闭重定向，因为底层只允许GET和HEAD，不允许POST等，
        HttpURLConnection.setFollowRedirects(false);

        // 设置ssl证书处理，避免证书问题导致异常
        try {
            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new X509TrustManagerExt()};
            sslcontext.init(null, tm, new SecureRandom());
            HostnameVerifier ignoreHostnameVerifier = (s, sslsession) -> {
                System.out.println("WARNING: Hostname is not matched for cert.");
                return true;
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    /**
     * GET获取url内容，并返回
     *
     * @param strUrl url
     * @return 返回的api内容或html
     */
    public static String GetPage(String strUrl) {
        return GetPage(strUrl, _defaultConfig);
    }

    /**
     * GET获取url内容，并返回
     *
     * @param strUrl url
     * @param param  GET的参数
     * @return 返回的api内容或html
     */
    public static String GetPage(String strUrl, String param) {
        Config config = InitConfig();
        config.setMethod("GET");
        config.setParam(param);
        return GetPage(strUrl, config);
    }

    /**
     * POST获取url内容，并返回
     *
     * @param strUrl url
     * @param param  POST的body
     * @return 返回的api内容或html
     */
    public static String PostPage(String strUrl, String param) {
        Config config = InitConfig();
        config.setMethod("POST");
        config.setParam(param);
        return GetPage(strUrl, config);
    }

    /**
     * 获取url内容，并返回
     *
     * @param strUrl url
     * @param config 请求配置
     * @return 返回的api内容或html
     */
    public static String GetPage(String strUrl, Config config) {
        return GetPage(strUrl, config, null, 0);
    }

    // 主调方法
    private static String GetPage(String strUrl, Config config, String originUrl, int deep) {
        if (config == null)
            config = _defaultConfig;
        else if (StringUtils.isEmpty(config.getEncoding()))
            config.setEncoding("UTF-8");
        if (StringUtils.isEmpty(config.getUserAgent()))
            config.setUserAgent(USER_AGENT);

        String method = config.getMethod();
        if (StringUtils.isBlank(method))
            method = "GET";
        else
            method = method.trim().toUpperCase();// HTTP协议要求大写
        boolean isGet = method.equals("GET");
        String param = config.getParam();

        strUrl = ProcessUrl(strUrl, isGet ? param : "");

        HttpURLConnection connection = null;
        OutputStream os = null;

        try {
            URL url = new URL(strUrl);
            connection = OpenConnection(url, config.getProxy());
            connection.setRequestMethod(method);

            // todo: 用户名密码处理 request.Credentials = new NetworkCredential(userName, password);

            if (config.getConnectTimeout() > 0)
                connection.setConnectTimeout(config.getConnectTimeout());
            if (config.getReadTimeout() > 0)
                connection.setReadTimeout(config.getReadTimeout());

            SetHeaders(connection, config, method);

            // todo: 写cookie，设置proxy

            StringBuilder sb = new StringBuilder();
            if (config.isShowHeader()) {
                sb.append(method).append(" ");
                if (originUrl != null)
                    sb.append(originUrl).append(" -> ");
                sb.append(strUrl).append("\r\n请求头信息：\r\n");
                // getRequestProperties调用必须在getOutputStream或connect之前
                AppendHeader(sb, connection.getRequestProperties());
            }

            if (!isGet) {
                connection.setDoOutput(true);
                if (StringUtils.isNotBlank(param)) {
                    // POST数据写入
                    os = connection.getOutputStream();
                    os.write(param.getBytes());
                } else {
                    // 无参数时写入Content-Length, 不能用 connection.setRequestProperty("Content-Length", "0");
                    connection.setFixedLengthStreamingMode(0);
                }
            }

            connection.connect();

            int code = connection.getResponseCode();
            if ((code == 301 || code == 302) && config.isFollowRedirect()) {
                if (deep > 10)
                    throw new Exception(originUrl + ": 重定向次数超过10次");
                String location = connection.getHeaderField("Location");
                return DoRedirect(location, strUrl, config, originUrl, deep);
            }

            if (config.isShowHeader()) {
                sb.append("响应头信息：").append(code).append(" ").
                        append(connection.getResponseMessage()).append("\r\n");
                AppendHeader(sb, connection.getHeaderFields());
            }

            // 从响应里读取Cookie，并设置到默认_cookies里
            ParseCookie(strUrl, connection.getHeaderFields().get("Set-Cookie"));

            sb.append(GetResponse(connection));

            return sb.toString();
        } catch (Exception exp) {
            exp.printStackTrace();
            return null;
        } finally {
            Close(connection);
            Close(os);
        }
    }

    private static Config InitConfig() {
        Config config = new Config();
        config.setMethod("GET");
        config.setEncoding("UTF-8");
        config.setFollowRedirect(true);
        config.setCatchExp(true);

        // todo: 要注释
//        config.setShowHeader(true);
//        config.setProxy("127.0.0.1:8888");

        return config;
    }

    private static String ProcessUrl(String strUrl, String param) {
        //region url判断处理
        if (StringUtils.isEmpty(strUrl))
            throw new IllegalArgumentException("url can't be empty.");

        strUrl = strUrl.trim();
        if (StringUtils.indexOfIgnoreCase(strUrl, "http://") != 0 &&
                StringUtils.indexOfIgnoreCase(strUrl, "https://") != 0)
            //throw new IllegalArgumentException("url must be http protocol.");
            strUrl = "http://" + strUrl;

        // 删除网址后面的#号
        int idx = strUrl.indexOf('#');
        if (idx >= 0)
            strUrl = strUrl.substring(0, idx);

        // StringUtils.equals()
        if (StringUtils.isNotBlank(param)) {
            if (strUrl.indexOf('?') < 0)
                strUrl += "?" + param;
            else
                strUrl += "&" + param;
        }

        return strUrl;
    }

    private static HttpURLConnection OpenConnection(URL url, String proxy) throws IOException {
        HttpURLConnection ret = null;
        if (StringUtils.isNotEmpty(proxy)) {
            Proxy pr = null;
            String[] arr = proxy.split(":");
            if (arr.length == 2)
                pr = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(arr[0], Integer.parseInt(arr[1])));
            else if (arr.length == 1)
                pr = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(arr[0], 80));
            if (pr != null)
                ret = (HttpURLConnection) url.openConnection(pr);
        }
        if (ret == null)
            ret = (HttpURLConnection) url.openConnection();

        return ret;
    }

    private static void SetHeaders(HttpURLConnection connection, Config config, String method) {
        boolean contentTypeSetted = false;
        boolean cookieSetted = false;
        Map<String, String> headers = config.getHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                connection.setRequestProperty(key, entry.getValue());

                if (StringUtils.equalsIgnoreCase("Content-Type", key))
                    contentTypeSetted = true;
                if (StringUtils.equalsIgnoreCase("Cookie", key))
                    cookieSetted = true;
            }
        }
        for (Map.Entry<String, String> entry : _defaultHeader.entrySet()) {
            if (headers == null || !headers.containsKey(entry.getKey()))
                connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        if (StringUtils.isNotEmpty(config.getReferer()))
            connection.setRequestProperty("Referer", config.getReferer());
        if (StringUtils.isNotEmpty(config.getUserAgent()))
            connection.setRequestProperty("User-Agent", config.getUserAgent());

        // POST/PUT/DELETE 默认采用form data方式
        if (!contentTypeSetted && !method.equals("GET"))
            connection.setRequestProperty("Content-Type", "application/json");
        //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 设置默认Cookie
        if (!cookieSetted) {
            String cookies = CombineCookie(connection.getURL().toString());
            if (StringUtils.isNotEmpty(cookies))
                connection.setRequestProperty("Cookie", cookies);
        }
    }

    /**
     * 拼接组装当前请求要使用的Cookie
     *
     * @param url
     * @return
     */
    private static String CombineCookie(String url) {
        StringBuilder sb = new StringBuilder();
        String domain = GetDomain(url);
        boolean isSsl = StringUtils.startsWithIgnoreCase(url, "https://");

        List<String> removeKeys = new ArrayList<>();
        synchronized (_cookies) {
            for (Map.Entry<String, HttpCookie> entry : _cookies.entrySet()) {
                HttpCookie cook = entry.getValue();
                if (cook.hasExpired()) {
                    // 过期了, 收集进行删除
                    removeKeys.add(entry.getKey());
                    continue;
                }

                // 仅https可用
                if (cook.getSecure() && !isSsl)
                    continue;
                // 属于子域时
                String cookDomain = cook.getDomain();
                if (StringUtils.isEmpty(cookDomain) || StringUtils.containsIgnoreCase(domain, cookDomain))
                    sb.append(cook.getName()).append("=").append(cook.getValue()).append(";");
            }
            for (String key : removeKeys)
                _cookies.remove(key);
        }
        return sb.toString();
    }

    private static void AppendHeader(StringBuilder sb, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            sb.append("  ");
            String key = entry.getKey();
            if (key != null) // 响应header有key为空，value为 HTTP/1.1 200 OK
                sb.append(entry.getKey()).append("=");
            sb.append(entry.getValue()).append("\r\n");
        }
        sb.append("\r\n");
    }

    private static String DoRedirect(String redirectUrl, String currentUrl, Config config,
                                     String originUrl, int deep) throws CloneNotSupportedException {
        Config redirectConfig = config.clone();
        // 跳转只允许GET
        redirectConfig.setMethod("GET");
        redirectConfig.setParam("");
        redirectConfig.setReferer(currentUrl);
        // 如果跳转，递归调用. 这里的Location不区分大小写
        redirectUrl = GetRedirectUrl(redirectUrl, currentUrl);
        if (StringUtils.isNotEmpty(redirectUrl)) {
            originUrl = originUrl == null ? currentUrl : originUrl;
            return GetPage(redirectUrl, redirectConfig, originUrl, deep + 1);
        }
        return null;
    }

    private static String GetRedirectUrl(String redirectUrl, String originUrl) {
        if (StringUtils.isEmpty(redirectUrl))
            return null;
        if (StringUtils.startsWithIgnoreCase(redirectUrl, "http://"))
            return redirectUrl;
        if (StringUtils.startsWithIgnoreCase(redirectUrl, "https://"))
            return redirectUrl;
        return GetDomain(originUrl) + redirectUrl;
    }

    private static String GetDomain(String url) {
        int idx = url.indexOf('/', 8); // 跳过https://
        if (idx > 0) {
            return url.substring(0, idx);
        }
        return url;
    }

    private static String GetResponse(HttpURLConnection connection) throws IOException {
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            int code = connection.getResponseCode();
            if (code <= 399) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            if (is == null)
                return "";

            String contentEncoding = connection.getContentEncoding();
            if (contentEncoding != null && contentEncoding.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            // 这里不用BufferReader.readLine 避免自己加换行
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1)
                sb.append((char) c);
            return sb.toString();
        } finally {
            Close(reader, is);
        }
    }

    private static void ParseCookie(String url, List<String> arrCookies) {
        if (arrCookies == null)
            return;
        String domain = GetDomain(url);
        for (String item : arrCookies) {
            HttpCookie cookie = HttpCookie.parse(item).get(0);
            if (StringUtils.isNotEmpty(cookie.getDomain()))
                domain = cookie.getDomain();
            else
                cookie.setDomain(domain);
            String key = domain + ":" + cookie.getName();
            synchronized (_cookies) {
                if (_cookies.containsKey(key))
                    _cookies.replace(key, cookie);
                else
                    _cookies.put(key, cookie);
            }
        }
    }

    /**
     * 批量关闭对象
     *
     * @param arrObj 对象
     */
    private static void Close(Closeable... arrObj) {
        if (arrObj != null) {
            for (Closeable item : arrObj) {
                try {
                    if (item != null)
                        item.close();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
    }

    /**
     * 批量关闭对象
     *
     * @param arrObj 对象
     */
    private static void Close(HttpURLConnection... arrObj) {
        if (arrObj != null) {
            for (HttpURLConnection item : arrObj) {
                try {
                    if (item != null)
                        item.disconnect();
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
    }

    /**
     * 发起请求的配置项
     */
    @Data
    public static class Config implements Cloneable {
        /**
         * 请求方法：GET/POST/PUT/DELETE
         */
        private String method;
        /**
         * 本次请求的参数
         */
        private String param;
        /**
         *
         */
        private String encoding;
        /**
         * 本次请求的来源信息
         */
        private String referer;
        /**
         * 本次请求的连接超时时长，毫秒
         */
        private int connectTimeout;
        /**
         * 本次请求读取数据的等待超时时长，毫秒
         */
        private int readTimeout;
        /**
         * 返回的请求结果，是否需要包含请求头和响应头信息
         */
        private boolean showHeader;
        /**
         * 本次请求要使用的用户名
         */
        private String userName;
        /**
         * 本次请求要使用的密码
         */
        private String password;
        /**
         * 本次请求使用的代理，如 10.1.2.3:8080
         */
        private String proxy;
        /**
         * 本次请求使用的User-Agent
         */
        private String userAgent;
        /**
         * 是否要跟随301/302跳转
         */
        private boolean followRedirect;
        /**
         * 本次请求要设置的请求头信息
         */
        private Map<String, String> headers;
        private boolean catchExp;

        public Config clone() throws CloneNotSupportedException {
            return (Config) super.clone();
        }
    }

}
