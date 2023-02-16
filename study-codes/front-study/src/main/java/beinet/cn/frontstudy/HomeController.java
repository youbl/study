package beinet.cn.frontstudy;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final Counter myCounter;
    private final Gauge myGauge;
    private final Summary mySummary;

    private int idx = 1;

    @GetMapping("/list")
    public List<Integer> toList(@RequestParam(value = "ids", required = false) String idsStr) {
        Summary.Timer timer = mySummary.labels(idsStr).startTimer();

        if (idsStr == null)
            idsStr = "";
        List<Integer> idList = Arrays.stream(idsStr.split(","))
                .filter(s -> s.trim().length() > 0)
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        sleep(123 * idx); // 刷新10次，可以看50分位和95分位
        idx++;

        // 添加自定义埋点
        myCounter.labels(idsStr, idsStr + "2222").inc();
        myGauge.labels(idsStr).set(System.nanoTime());
        timer.observeDuration();
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

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
