package beinet.cn.springcachestudy.controller;

import beinet.cn.springcachestudy.service.BusinessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/4 11:51
 */
@RestController
public class HomeController {
    private final BusinessService businessService;

    public HomeController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("")
    public String index() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }


    @GetMapping("cache")
    public String getCache(@RequestParam int id) {
        String result = businessService.getById1(id, "");
        result += " <br>" + businessService.getById2(id, "");
        return result;
    }


}
