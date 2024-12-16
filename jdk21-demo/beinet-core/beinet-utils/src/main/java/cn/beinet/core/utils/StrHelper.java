package cn.beinet.core.utils;

/**
 * 新类
 * @author youbl
 * @since 2024/12/2 20:45
 */
public final class StrHelper {
    public static String trim(String originStr, String trimStr) {
        originStr = originStr.trim();
        int len = trimStr.length();
        while (originStr.startsWith(trimStr)) {
            originStr = originStr.substring(len).trim();
        }
        while (originStr.endsWith(trimStr)) {
            originStr = originStr.substring(0, originStr.length() - len).trim();
        }
        return originStr;
    }
}
