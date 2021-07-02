package beinet.cn.frontstudy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping("front")
    public String firstDemo(Model model) {
        model.addAttribute("title", "Thymeleaf页面演示");
        model.addAttribute("body", "<b>Hello thymeleaf.</b>");
        return "firstTh";
    }
}
