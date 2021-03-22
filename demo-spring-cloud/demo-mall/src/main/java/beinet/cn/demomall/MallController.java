package beinet.cn.demomall;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MallController {
    @GetMapping("/mall/{id}")
    public ProductDto get(@PathVariable int id) {
        return ProductDto.builder()
                .id(id)
                .name("产品" + id)
                .num(100)
                .build();
    }
}
