package cn.beinet.utils;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 此类用于解决ssl证书信任问题，比如通过Fiddler访问https报错：
 * unable to find valid certification path to requested target
 */
public class X509TrustManagerExt implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
