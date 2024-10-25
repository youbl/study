package beinet.cn.algorithmdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 欧几里得算法
 * @author youbl
 * @since 2024/10/25 14:27
 */
@RestController
@Api(tags = "写一些欧几里得算法的代码")
@Slf4j
public class EuclidController {

    @GetMapping("prime")
    @ApiOperation(value = "输入1个数字，返回是否质数")
    public boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        if (num <= 3) {
            return true;
        }
        // 提前排除2的倍数, 提升效率
        if (num % 2 == 0) {
            return false;
        }
        double sqrt = Math.sqrt(num);
        // 步跳为2，提升效率
        for (int i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) {
                log.info("{} % {} = 0", num, i);
                return false;
            }
        }
        log.info("{} 是质数", num);
        return true;
    }

    @GetMapping("primeDivisors")
    @ApiOperation(value = "输入1个数字，返回它的所有是质数的约数")
    public List<Integer> getPrimeDivisors(int num) {
        Assert.isTrue(num > 1, "num必须大于1");

        List<Integer> primeFactors = new ArrayList<>();
        if (num % 2 == 0) {
            primeFactors.add(2);
        }
        // 步跳为2，提升效率
        for (int i = 3; i <= num; i += 2) {
            if (num % i == 0 && isPrime(i)) {
                primeFactors.add(i);
            }
        }
        return primeFactors;
    }

}
