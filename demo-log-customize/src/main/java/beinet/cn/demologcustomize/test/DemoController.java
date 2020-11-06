package beinet.cn.demologcustomize.test;

import beinet.cn.demologcustomize.NeedLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/6 14:32
 */
@RestController
public class DemoController {
    /**
     * 正常的日志记录
     *
     * @param name
     * @return
     */
    @GetMapping("index")
    @NeedLog
    public String index(@RequestParam(required = false) String name) {
        return "Hello, " + (name == null || name.isEmpty() ? "匿名者" : name);
    }

    /**
     * 出现异常时，把异常也记录下来
     *
     * @param name
     * @return
     */
    @GetMapping("index2")
    @NeedLog(logExceptionAsError = true)
    public String index2(@RequestParam(required = false) String name) {
        Integer.parseInt(name);
        return "Hello, " + (name == null || name.isEmpty() ? "匿名者" : name);
    }

}
