package beinet.cn.demoproduct;

import beinet.cn.demoproduct.config.IpInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ProductController {
    private final IpInfo ipInfo;

    public ProductController(IpInfo ipInfo) {
        this.ipInfo = ipInfo;
    }

    @GetMapping("/")
    public String index() {
        String ret = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " 我是Product产品服务首页";
        ret += " " + ipInfo.getServerInfo();
        return ret;
    }

    @GetMapping("/product/{id}")
    public ProductDto get(@PathVariable int id) {
        return ProductDto.builder()
                .id(id)
                .name("产品" + id)
                .num(100)
                .creationTime(LocalDateTime.now())
                .build();
    }
}
