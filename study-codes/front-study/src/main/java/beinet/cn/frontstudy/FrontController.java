package beinet.cn.frontstudy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FrontController {
    @GetMapping("front")
    public String firstDemo(Model model) {
        model.addAttribute("title", "Thymeleaf页面演示");
        model.addAttribute("body", "<b>Hello thymeleaf.</b>");

        List<User> datas = new ArrayList<>();
        datas.add(new User(11, "张三"));
        datas.add(new User(121, "李四"));
        model.addAttribute("dataList", datas);

        model.addAttribute("nowTime", LocalDateTime.now());
        return "firstTh";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private int id;
        private String name;
    }
}
