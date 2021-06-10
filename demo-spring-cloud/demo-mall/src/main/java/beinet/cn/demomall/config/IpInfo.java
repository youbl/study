package beinet.cn.demomall.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class IpInfo implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    /**
     * 获取当前服务IP
     *
     * @return IP
     */
    public String getServerIp() {
        InetAddress address = null;
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return e.getMessage();
        }
    }

    /**
     * 获取当前服务监听的端口
     *
     * @return 端口
     */
    public int getServerPort() {
        return this.serverPort;
    }

    public String getServerInfo() {
        return getServerIp() + ":" + String.valueOf(getServerPort());
    }
}
