package beinet.cn.algorithmdemo.controller;

import beinet.cn.algorithmdemo.controller.dto.CompareResult;
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

    /**
     * 1千万的测试结果：
     * {
     *   "result": true,
     *   "costMillis1": 1401,
     *   "costMillis2": 1040
     * 1亿的测试结果：
     * {
     *   "result": true,
     *   "costMillis1": 37471,
     *   "costMillis2": 26095
     */
    @GetMapping("primeCompare")
    @ApiOperation(value = "验证新旧2个质数方法的正确性和时长")
    public CompareResult primeCompare() {
        int maxNum = 100000000;

        StringBuilder p1 = new StringBuilder();
        long start1 = System.currentTimeMillis();
        for (int i = 1; i < maxNum; i++) {
            p1.append(isPrimeOld(i) ? "1" : "0");
        }
        long cost1 = System.currentTimeMillis() - start1;

        StringBuilder p2 = new StringBuilder();
        long start2 = System.currentTimeMillis();
        for (int i = 1; i < maxNum; i++) {
            p2.append(isPrime(i) ? "1" : "0");
        }
        long cost2 = System.currentTimeMillis() - start2;

        return new CompareResult()
                .setCostMillis1(cost1)
                .setCostMillis2(cost2)
                .setResult(p1.toString().contentEquals(p2));
    }

    @GetMapping("primeOld")
    @ApiOperation(value = "输入1个数字，返回是否质数")
    public boolean isPrimeOld(int num) {
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
                //log.info("{} % {} = 0", num, i);
                return false;
            }
        }
        //log.info("{} 是质数", num);
        return true;
    }

    @GetMapping("prime")
    @ApiOperation(value = "输入1个数字，返回是否质数")
    public boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        if (num <= 3) {
            return true;
        }
        // 提前排除2和3的倍数, 提升效率
        if (num % 2 == 0 || num % 3 == 0) {
            return false;
        }
        int sqrt = (int) Math.sqrt(num);
        // 所有大于6的质数，都可以表示为6x+1，因此可以设置步跳为6，减少循环，提升效率
        for (int i = 5; i <= sqrt; i += 6) {
            if (num % i == 0) {
                //log.info("{} % {} = 0", num, i);
                return false;
            }
            if (num % (i + 2) == 0) {
                //log.info("{} % {} = 0", num, i + 2);
                return false;
            }
        }
        //log.info("{} 是质数", num);
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
