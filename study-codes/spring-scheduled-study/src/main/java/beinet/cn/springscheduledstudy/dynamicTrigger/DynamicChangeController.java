package beinet.cn.springscheduledstudy.dynamicTrigger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 修改定时任务间隔的控制器
 * @author youbl
 * @since 2025/4/9 14:45
 */
@RestController
@Slf4j
public class DynamicChangeController {

    @GetMapping("updateTimer")
    public String updateTimer(@RequestParam long millis) {
        long old = DynamicVars.getTimerMillis();
        DynamicVars.setTimerMillis(millis);
        String ret = "定时器间隔修改：" + old + " => " + millis;
        log.info(ret);
        return ret;
    }
}
