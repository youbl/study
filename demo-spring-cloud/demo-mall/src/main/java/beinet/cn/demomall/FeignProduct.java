package beinet.cn.demomall;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "feignProduct", url = "http://localhost:9004/product")
public interface FeignProduct {
    @GetMapping("{id}")
    ProductDto get(@PathVariable int id);
}
