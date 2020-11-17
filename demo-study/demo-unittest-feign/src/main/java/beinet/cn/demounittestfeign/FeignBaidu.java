package beinet.cn.demounittestfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FeignBaidu
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 17:55
 */
@FeignClient(name = "baidu", url = "https://www.baidu.com/")
public interface FeignBaidu {

    /**
     * 访问百度首页
     *
     * @return html
     */
    @GetMapping(value = "",
            headers = {"User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"})
    String index();


    /**
     * 访问百度搜索页
     *
     * @param wd 搜索词
     * @return html
     */
    @GetMapping(value = "s",
            headers = {"User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"})
    String search(@RequestParam String wd);
}
