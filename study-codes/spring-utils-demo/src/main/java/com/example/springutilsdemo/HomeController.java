package com.example.springutilsdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 11:52
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String index() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }

    // @RequestMapping("*") 这个注解会把html静态资源也拦截，不能用
    public String is404() {
        // 所有404请求，都要到这里
        return "404页面找不到";
    }

    @GetMapping("header")
    public static Map<String, List<String>> getHeaders(@RequestParam String url) {
        Map<String, List<String>>  ret = HttpUtils.getHeaders(url);
        return ret;
    }
}
