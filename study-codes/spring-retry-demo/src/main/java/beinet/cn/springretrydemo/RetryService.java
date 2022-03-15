package beinet.cn.springretrydemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/15 15:26
 */
@Service
@Slf4j
public class RetryService {
    private int idx;

    @Retryable(value = Exception.class, maxAttempts = 4)
    public String retry() throws Exception {
        idx++;
        log.info("第" + idx + "次访问，准备抛异常");
        throw new Exception("第" + idx + "次访问，我抛了异常");
    }
}
