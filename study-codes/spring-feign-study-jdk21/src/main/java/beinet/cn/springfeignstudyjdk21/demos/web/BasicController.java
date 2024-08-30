package beinet.cn.springfeignstudyjdk21.demos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("")
    public String hello() {
        return "Hello world";
    }

}
