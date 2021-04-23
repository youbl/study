package beinet.cn.frontstudy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HomeController {
    @GetMapping("/list")
    public List<Integer> toList(@RequestParam(value = "ids", required = false) String idsStr) {
        if (idsStr == null)
            idsStr = "";
        List<Integer> idList = Arrays.stream(idsStr.split(","))
                .filter(s -> s.trim().length() > 0)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        return idList;
    }

    @GetMapping("/arr")
    public Integer[] toArray(@RequestParam(value = "ids", required = false) String idsStr) {
        if (idsStr == null)
            idsStr = "";
        return Arrays.stream(idsStr.split(","))
                .filter(s -> s.trim().length() > 0)
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
