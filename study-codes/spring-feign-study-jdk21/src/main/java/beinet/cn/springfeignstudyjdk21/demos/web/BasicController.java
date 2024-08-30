package beinet.cn.springfeignstudyjdk21.demos.web;

import beinet.cn.springfeignstudyjdk21.demos.feigns.FeignDemo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BasicController {
    private final FeignDemo feignDemo;

    @GetMapping(value = "", produces = {"text/plain"})
    public String hello() {
        return feignDemo.xxx();
    }

}
