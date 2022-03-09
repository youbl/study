package beinet.cn.springmaildemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/8 20:16
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String index() {
        return this.getClass().getName();
    }
}