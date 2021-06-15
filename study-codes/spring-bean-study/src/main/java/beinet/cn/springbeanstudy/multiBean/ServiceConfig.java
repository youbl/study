package beinet.cn.springbeanstudy.multiBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean("AServiceCls")
    public ServiceCls AServiceCls() {
        return new ServiceCls("我是AServiceCls");
    }

    @Bean("BServiceCls")
    public ServiceCls BServiceCls() {
        return new ServiceCls("我是BServiceCls");
    }

    public class ServiceCls {
        private String content;

        public ServiceCls(String content) {
            this.content = content;
        }

        public void run() {
            System.out.println(this + " " + content);
        }
    }
}
