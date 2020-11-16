package beinet.cn.demounittestmockmvc;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DbController {

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
    }


    @GetMapping("hello")
    public String testHello(@RequestParam String query1, HttpServletRequest request) {
        String merchantId = request.getHeader("x-MERCHANT-ID");
        return "Hello," + query1 + merchantId;
    }


    @PostMapping("hello")
    public Dto testPostHello(@RequestBody Dto dto, HttpServletRequest request) {
        String merchantId = request.getHeader("x-MERCHANT-ID");
        if (dto == null) {
            dto = new Dto();
        }
        dto.setDesc(dto.getDesc() + "-" + merchantId);
        return dto;
    }
}
