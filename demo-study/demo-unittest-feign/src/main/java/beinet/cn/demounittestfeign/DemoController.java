package beinet.cn.demounittestfeign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DemoController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 17:58
 */
@RestController
public class DemoController {
    private final FeignBaidu feignBaidu;

    public DemoController(FeignBaidu feignBaidu) {
        this.feignBaidu = feignBaidu;
    }

    @GetMapping("baidu")
    public String getBaidu() {
        return HtmlUtils.htmlEscape(feignBaidu.search("beinet"));
    }

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
    }
}
