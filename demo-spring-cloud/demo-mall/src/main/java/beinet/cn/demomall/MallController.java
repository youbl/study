package beinet.cn.demomall;

import beinet.cn.demomall.config.IpInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class MallController {
    private final FeignProduct feignProduct;
    private final IpInfo ipInfo;

    public MallController(FeignProduct feignProduct, IpInfo ipInfo) {
        this.feignProduct = feignProduct;
        this.ipInfo = ipInfo;
    }

    @GetMapping("/")
    public String index() {
        String ret = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " 我是Mall商城首页";
        ret += " " + ipInfo.getServerInfo();
        return ret;
    }

    @GetMapping("/mall/{id}")
    public ProductDto get(@PathVariable int id) {
        ProductDto dto = feignProduct.get(id);
        dto.setMallName("贝可商城");
        dto.setMallTime(LocalDateTime.now());
        return dto;
    }
}
