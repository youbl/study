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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        long maxNum = 100000000;

        StringBuilder p1 = new StringBuilder();
        long start1 = System.currentTimeMillis();
        for (long i = 1; i < maxNum; i++) {
            p1.append(isPrimeOld(i) ? "1" : "0");
        }
        long cost1 = System.currentTimeMillis() - start1;

        StringBuilder p2 = new StringBuilder();
        long start2 = System.currentTimeMillis();
        for (long i = 1; i < maxNum; i++) {
            p2.append(isPrime(i) ? "1" : "0");
        }
        long cost2 = System.currentTimeMillis() - start2;

        return new CompareResult()
                .setCostMillis1(cost1)
                .setCostMillis2(cost2)
                .setResult(p1.toString().contentEquals(p2));
    }

    @GetMapping("primeOld")
    @ApiOperation(value = "输入1个数字，返回是否质数;性能略低")
    public boolean isPrimeOld(long num) {
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
        for (long i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) {
                //log.info("{} % {} = 0", num, i);
                return false;
            }
        }
        //log.info("{} 是质数", num);
        return true;
    }

    @GetMapping("prime")
    @ApiOperation(value = "输入1个数字，返回是否质数；性能略好")
    public boolean isPrime(long num) {
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
        long sqrt = (long) Math.sqrt(num);
        // 所有大于6的质数，都可以表示为6x+1，因此可以设置步跳为6，减少循环，提升效率
        for (long i = 5; i <= sqrt; i += 6) {
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
    public DivisorResult getPrimeDivisors(long num) {
        Assert.isTrue(num > 1, "num必须大于1");

        StringBuilder mulStr = new StringBuilder();
        mulStr.append(num).append(" = 1");
        Set<Long> primeFactors = new HashSet<>();
        // 把2的因数消除
        while (num % 2 == 0) {
            primeFactors.add(2L);
            num /= 2;
            mulStr.append(" * ").append(2);
        }
        // 步跳为3，提升效率
        long sqrt = (long) Math.sqrt(num);
        for (long i = 3; i <= sqrt; i += 2) {
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
    public long euler(long num) {
        Assert.isTrue(num > 1, "num必须大于1");
        DivisorResult divisorResult = getPrimeDivisors(num);

        // 欧拉函数公式：假设num有n个是质数的约数，分别为 p1 p2 ... pn
        // 则小于等于它的正数数中，与num互质的数字个数为： num * (1-1/p1) * (1-1/p2) * ... * (1-1/pn)
        long ret = num;
        for (long prime : divisorResult.getPrime()) {
            // ret = ret * (1 - 1D / prime); // 会产生小数，把它拆开成下面的算式
            ret = ret * (prime - 1) / prime;
        }

        return ret;
    }

    @GetMapping("euclid")
    @ApiOperation(value = "欧几里得算法，求解2个正整数的最大公约数")
    public long euclid(long a, long b) {
        // 让a存储较大的数字
        if (a < b) {
            long temp = a;
            a = b;
            b = temp;
        }
        while (b > 0) {
            long r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    @GetMapping("euclidExtend")
    @ApiOperation(value = "扩展欧几里得算法，求解2个正整数的最大公约数z，并得出另2个整数x和y，使得ax+by=z。注：x和y可为负数")
    public EuclidExtResult euclidExtend(long a, long b) {
        // 让a存储较大的数字
        if (a < b) {
            long temp = a;
            a = b;
            b = temp;
        }
        long originA = a;
        long originB = b;

        long x1 = 0, x2 = 1, y1 = 1, y2 = 0;
        while (b > 0) {
            long div = a / b;    // 除法结果
            long remain = a % b; // 余数
            a = b;
            b = remain;
            // 更新系数
            long x = x2 - div * x1;
            long y = y2 - div * y1;
            x2 = x1;
            y2 = y1;
            x1 = x;
            y1 = y;
        }
        return new EuclidExtResult()
                .setGreatCommonDivisor(a)
                .setBigFactor(x2)
                .setSmallFactor(y2)
                .setBigNum(originA)
                .setSmallNum(originB)
                .setFormula(x2 + " * " + originA + " + " + y2 + " * " + originB + " = " + a);
    }

    @GetMapping("congruence")
    @ApiOperation(value = "找到让指定的2个数字a b同余的m列表",
            notes = "同余指给定3个数字:a,b,m，如果(a-b)/m能整除，则称整数a与b对模m同余，记作a≡b (mod m)；也可以用a%m=b%m来判定")
    public List<String> findCongruence(long a, long b) {
        List<String> mods = new ArrayList<>();
        long diff = Math.abs(a - b);

        // 找出 2 到 diff 的所有因数，不考虑1，因为mod 1的余数都是0
        for (long i = 2; i <= diff; i++) {
            if (diff % i == 0) {
                String equal = a + " % " + i + " = " + b + " % " + i;
                mods.add(equal + " = " + (a % i));
//                String valA = a + " / " + i + " = " + (a / i);
//                String valB = b + " / " + i + " = " + (b / i);
//                mods.add(valA);
//                mods.add(valB);
            }
        }

        return mods;
    }

    @GetMapping("congruenceFunc")
    @ApiOperation(value = "给定3个整数，求对应线性同余方程的解",
            notes = "线性同余方程指：ax≡b (mod m)，在给定a,b,m时，求解x的值")
    public String findCongruenceResult(long a, long b, long m) {
        // 计算a 和 m的最大公约数 gcd，就是greatest common divisor
        EuclidExtResult euclidResult = euclidExtend(a, m);
        long gcd = euclidResult.getGreatCommonDivisor();

        // 如果b不能被gcd整除，则方程无解
        if ((b % gcd) != 0) {
            return (a + "x≡" + b + " (mod " + m + ") 此线性方程无解");
        }
        // 使用扩展欧几里算法得到的系数x和y，使得 ax+my=gcd，然后通过调整x来找到满足αx≡b(mod m)的解
        long x, y;
        if (euclidResult.getSmallNum().equals(a)) {
            // 扩展欧几里算法里a和m发生了交换，因此x和y的附属关系要调整
            x = euclidResult.getSmallFactor();
            y = euclidResult.getBigFactor();
        } else {
            x = euclidResult.getBigFactor();
            y = euclidResult.getSmallFactor();
        }
        long x0 = (b / gcd) * x % m;
        // 确保 x0 是正数
        if (x0 < 0) {
            x0 += m;
        }
        return String.valueOf(x0);
    }
}
