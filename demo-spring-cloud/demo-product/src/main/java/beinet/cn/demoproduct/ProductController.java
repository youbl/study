package beinet.cn.demoproduct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ProductController {
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
