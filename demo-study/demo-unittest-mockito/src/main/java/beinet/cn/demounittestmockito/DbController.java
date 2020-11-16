package beinet.cn.demounittestmockito;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DbController {

    private final BusinessService businessService;

    public DbController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
    }

    @GetMapping("baidu")
    public String getBaidu() {
        return businessService.requestBaiduHtml();
    }

    @GetMapping("sina")
    public String getSina() {
        return businessService.requestSinaHtml();
    }

    @GetMapping("para")
    public String getWithPara(@RequestParam String key) {
        return businessService.requestByPara(key);
    }

    @GetMapping("exp")
    public String getExp() {
        businessService.throwExp(this);
        return "";
    }
}
