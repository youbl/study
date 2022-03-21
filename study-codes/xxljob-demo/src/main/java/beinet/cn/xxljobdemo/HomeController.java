package beinet.cn.xxljobdemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/8 20:16
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @GetMapping("")
    public String index() {
        return this.getClass().getName();
    }
}