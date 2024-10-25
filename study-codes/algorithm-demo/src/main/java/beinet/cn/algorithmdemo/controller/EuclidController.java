package beinet.cn.algorithmdemo.controller;

import beinet.cn.algorithmdemo.controller.dto.CompareResult;
import beinet.cn.algorithmdemo.controller.dto.DivisorResult;
import beinet.cn.algorithmdemo.controller.dto.EuclidExtResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

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
    public DivisorResult getPrimeDivisors(int num) {
        Assert.isTrue(num > 1, "num必须大于1");

        StringBuilder mulStr = new StringBuilder();
        mulStr.append(num).append(" = 1");
        Set<Integer> primeFactors = new HashSet<>();
        // 把2的因数消除
        while (num % 2 == 0) {
            primeFactors.add(2);
            num /= 2;
            mulStr.append(" * ").append(2);
        }
        // 步跳为3，提升效率
        int sqrt = (int) Math.sqrt(num);
        for (int i = 3; i <= sqrt; i += 2) {
            while (num % i == 0) {
                primeFactors.add(i);
                num /= i;
                mulStr.append(" * ").append(i);
            }
        }
        // 如果num本身是一个质数
        if (num > 2) {
            primeFactors.add(num);
            mulStr.append(" * ").append(num);
        }
        return new DivisorResult()
                .setMul(mulStr.toString())
                .setPrime(primeFactors);
    }

    @GetMapping("euler")
    @ApiOperation(value = "欧拉函数：输入1个数字，返回结果：小于等于它的正整数里，有多少个与它互质", notes = "互质定义：2个数除1之外，没有第2个公约数")
    public int euler(int num) {
        Assert.isTrue(num > 1, "num必须大于1");
        DivisorResult divisorResult = getPrimeDivisors(num);

        // 欧拉函数公式：假设num有n个是质数的约数，分别为 p1 p2 ... pn
        // 则小于等于它的正数数中，与num互质的数字个数为： num * (1-1/p1) * (1-1/p2) * ... * (1-1/pn)
        int ret = num;
        for (int prime : divisorResult.getPrime()) {
            // ret = ret * (1 - 1D / prime); // 会产生小数，把它拆开成下面的算式
            ret = ret * (prime - 1) / prime;
        }

        return ret;
    }

    @GetMapping("euclid")
    @ApiOperation(value = "欧几里得算法，求解2个正整数的最大公约数")
    public int euclid(int a, int b) {
        // 让a存储较大的数字
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        while (b > 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    @GetMapping("euclidExtend")
    @ApiOperation(value = "扩展欧几里得算法，求解2个正整数的最大公约数z，并得出另2个整数x和y，使得ax+by=z。注：x和y可为负数")
    public EuclidExtResult euclidExtend(int a, int b) {
        // 让a存储较大的数字
        if (a < b) {
            int temp = a;
            a = b;
            b = temp;
        }
        int originA = a;
        int originB = b;

        int x1 = 0, x2 = 1, y1 = 1, y2 = 0;
        while (b > 0) {
            int div = a / b;    // 除法结果
            int remain = a % b; // 余数
            a = b;
            b = remain;
            // 更新系数
            int x = x2 - div * x1;
            int y = y2 - div * y1;
            x2 = x1;
            y2 = y1;
            x1 = x;
            y1 = y;
        }
        return new EuclidExtResult()
                .setBigDivisor(a)
                .setBigFactor(x2)
                .setSmallFactor(y2)
                .setFormula(x2 + " * " + originA + " + " + y2 + " * " + originB + " = " + a);
    }
}
