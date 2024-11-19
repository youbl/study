package cn.beinet.core.base;

import cn.beinet.core.base.configs.ConfigConst;
import cn.beinet.core.base.configs.SystemConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 输出一些启动基本信息
 *
 * @author youbl
 * @since 2024/7/18 16:13
 */
@Component
@Slf4j
public class StartInfo implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // beinet-admin:local 启动. sysinfo:Windows 11,10.0,amd64 innerIP:10.100.68.11 outerIP:8.217.76.130
        log.info("{}:{} 启动. sysinfo:{} innerIP:{} outerIP:{}",
                ConfigConst.getAppName(),
                ConfigConst.getActiveProfile(),
                SystemConst.getOsInfo(),
                SystemConst.getServerIp(),
                SystemConst.getOuterIp());
    }
}
