package beinet.cn.reflectionstudy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DemoController {
    ThreadLocal<Dto> threadLocal = new ThreadLocal<>();

    /**
     * 因为Web使用的是线程池，在这里使用ThreadLocal，且没有手工清理时，会出现上一个请求写入的数据，被下一请求读取到的问题，
     * 需要注意清理
     *
     * @return
     */
    @GetMapping("")
    public String get() {
        String ret = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + ": ";
        ret += "当前线程:" + Thread.currentThread().getId() + "  ";
        Dto dto = threadLocal.get();
        if (dto == null)
            ret += "取到空值; ";
        else
            ret += dto + "; ";
        threadLocal.set(new Dto(12345, "abcde"));
        return ret;
    }
}
