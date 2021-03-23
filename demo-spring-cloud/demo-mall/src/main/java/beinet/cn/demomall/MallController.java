package beinet.cn.demomall;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MallController {
    private final FeignProduct feignProduct;

    public MallController(FeignProduct feignProduct) {
        this.feignProduct = feignProduct;
    }

    @GetMapping("/mall/{id}")
    public ProductDto get(@PathVariable int id) {
        ProductDto dto = feignProduct.get(id);
        dto.setMallName("贝可商城");
        dto.setMallTime(LocalDateTime.now());
        return dto;
    }
}
