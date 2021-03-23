package beinet.cn.demomall;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "demo-product")  // 这个demo-product 必须是产品项目注册到Eureka里的应用名
public interface FeignProduct {
    @GetMapping("product/{id}")
    ProductDto get(@PathVariable int id);
}
