package beinet.cn.algorithmdemo.controller;

import beinet.cn.algorithmdemo.controller.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 欧几里得算法
 *
 * @author youbl
 * @since 2024/10/25 14:27
 */
@RestController
@Api(tags = "写一些欧几里得算法的代码")
@Slf4j
public class EuclidController {
    private final int RSA_BLOCK_SIZE = 2; // 示例块大小，实际使用时应根据模数大小调整

    /**
     * 1千万的测试结果：
     * {
     * "result": true,
     * "costMillis1": 1401,
     * "costMillis2": 1040
     * 1亿的测试结果：
     * {
     * "result": true,
     * "costMillis1": 37471,
     * "costMillis2": 26095
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
    public CongruenceResult findCongruenceResult(long a, long b, long m) {
        // 计算a 和 m的最大公约数 gcd，就是greatest common divisor
        EuclidExtResult euclidResult = euclidExtend(a, m);
        long gcd = euclidResult.getGreatCommonDivisor();

        // 如果b不能被gcd整除，则方程无解
        if ((b % gcd) != 0) {
            return CongruenceResult.fail(a + "x≡" + b + " (mod " + m + ") 此线性方程无解");
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
        return CongruenceResult.ok(x0);
    }

    /**
     * 使用输入的2个质数，生成该2个质数对应的rsa公钥和私钥。
     * 输入可以为0，表示随机找2个质数
     *
     * @param p 质数1
     * @param q 质数2
     * @return rsa公 私钥
     */
    @GetMapping("countRsaKey")
    @ApiOperation(value = "给定2个质数，计算对应的RSA公钥和私钥", notes = "随机2个很大的质数，其积n加一人固定的e就是公钥；n加线性同余方程解d就是私钥")
    public RsaKeyResult rsaKeyCount(@RequestParam(required = false) Long p,
                                    @RequestParam(required = false) Long q) {
        if (p == null) {
            p = 61L;
        }
        if (q == null) {
            q = 53L;
        }
        if (!isPrime(p)) {
            throw new RuntimeException(p + " 不是质数");
        }
        if (!isPrime(q)) {
            throw new RuntimeException(q + " 不是质数");
        }

        // rsa非对称加密算法原理：
        // 1、选择2个特别大的不同的质数，就是上面的 p 和 q 。
        //    比如linux上的ssh-keygen -t rsa 默认是随机选择2个1024位(二进制位数)的素数。
        // 2、计算乘积 n = p * q
        long n = p * q;
        // 3、计算欧拉函数值，小于n的且与n互质的数字个数，可以用上面的 euler 方法计算
        //    一般用下面的公式计算，2个质数的积，对应的欧拉结果是 (p-1)(q-1)
        long euler = (p - 1) * (q - 1);
        // 4、选择一个公钥指数e，这具数字的取值区间： 1 < e <euler，且跟 euler 互质，
        //    e太小不安全，太大又性能差，一般固定使用 65537
        long e = 65537 > euler ? 17 : 65537;
        // 5、计算私钥指数d, 使得 d * e ≡ 1 (mod euler)
        //    可以通过上面的线性同余方程求出d
        CongruenceResult result = findCongruenceResult(e, 1, euler);
        if (!result.isOk()) {
            throw new RuntimeException(result.getErrMsg());
        }
        long d = result.getCongruence();

        // 6、ok，公钥就是 (n, e) 私钥就是 (n, d)
        String publicKey = "PublicKey=(" + n + "," + e + ")";
        String privateKey = "PrivateKey=(" + n + "," + d + ")";
        return new RsaKeyResult()
                .setP(p)
                .setQ(q)
                .setEuler(euler)
                .setN(n)
                .setE(e)
                .setD(d);
    }

    /**
     * 使用rsaKeyCount生成的的rsa公钥，对数据进行加密。
     *
     * @param n   公钥对里的数字n，参考 rsaKeyCount 方法里的注释
     * @param e   公钥对里的数字e，参考 rsaKeyCount 方法里的注释
     * @param str 要加密的数据
     * @return 加密后的结果
     */
    @GetMapping("rsaEncrypt")
    @ApiOperation(value = "使用rsaKeyCount生成的的rsa公钥，对数据进行加密。")
    public String rsaEncByPublicKey(@RequestParam long n,
                                    @RequestParam long e,
                                    @RequestParam String str) {
        // 加密过程
        // 1、把str转换为一个或多个整数，这些整数的大小要小于n
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        List<Long> strNums = convertToIntegers(strBytes, RSA_BLOCK_SIZE);
        log.info("加密前的整数：{}", strNums);

        // 2、加密运算，对于每个整数m，使用公式进行加密计算得到c：
        // c = m^e mod n
        List<Long> encedNums = new ArrayList<>();
        for (Long m : strNums) {
            long c = countPowAndMod(m, e, n);//((long) Math.pow(m, e)) % n;
            encedNums.add(c);
        }
        log.info("加密后的整数：{}", encedNums);

        // 3、转换成byte[]数组，再转换为字符串返回
        byte[] encedBytes = combineEncryptedLongs(encedNums, RSA_BLOCK_SIZE);
        log.info("加密后的字符数组：{}", encedBytes);
        return Base64.getEncoder().encodeToString(encedBytes);
    }

    /**
     * 使用rsaKeyCount生成的的rsa私钥，对数据进行解密。
     *
     * @param n   私钥对里的数字n，参考 rsaKeyCount 方法里的注释
     * @param d   私钥对里的数字d，参考 rsaKeyCount 方法里的注释
     * @param str 要解密的数据
     * @return 解密后的结果
     */
    @GetMapping("rsaDescrypt")
    @ApiOperation(value = "使用rsaKeyCount生成的的rsa私钥，对数据进行解密。")
    public String rsaDesByPrivateKey(@RequestParam long n,
                                     @RequestParam long d,
                                     @RequestParam String str) {
        // 解密过程，rsaEncByPublicKey的逆运算
        byte[] strBytes = Base64.getDecoder().decode(str);
        log.info("待解密的字符数组：{}", strBytes);
        List<Long> strNums = extractEncryptedLongs(strBytes, RSA_BLOCK_SIZE);
        log.info("解密前的整数：{}", strNums);

        // 2、解密运算，对于每个加密的整数c，使用公式进行解密计算得到m：
        // m = c^d mod n
        List<Long> desNums = new ArrayList<>();
        for (Long c : strNums) {
            long m = countPowAndMod(c, d, n);
            desNums.add(m);
        }
        log.info("解密后的整数：{}", desNums);

        // 3、转换成byte[]数组，再转换为字符串返回
        byte[] desBytes = convertToBytes(desNums, RSA_BLOCK_SIZE);
        return new String(desBytes, StandardCharsets.UTF_8);
    }

    // 一般要用BigInteger，这里简单处理，用long
    private static List<Long> convertToIntegers(byte[] data, int blockSize) {
        List<Long> integers = new ArrayList<>();
        for (int i = 0; i < data.length; i += blockSize) {
            // 计算当前块的实际大小
            int size = Math.min(blockSize, data.length - i);

            // 将字节块转换为整数
            long value = 0;
            for (int j = 0; j < size; j++) {
                value = (value << 8) | (data[i + j] & 0xFF);
            }
            //byte[] block = new byte[size];
            //System.arraycopy(data, i, block, 0, size);
            // BigInteger value = new BigInteger(1, block);

            integers.add(value);
        }
        return integers;
    }

    private static byte[] convertToBytes(List<Long> longs, int blockSize) {
        List<Byte> byteList = new ArrayList<>();

        for (Long value : longs) {
            for (int i = blockSize - 1; i >= 0; i--) {
                byteList.add((byte) ((value >> (8 * i)) & 0xFF));
            }
        }

        // 将 List<Byte> 转换为 byte[]
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }

    @SneakyThrows
    private static byte[] combineEncryptedLongs(List<Long> encryptedLongs, int blockSize) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (Long encryptedValue : encryptedLongs) {
            for (int i = blockSize - 1; i >= 0; i--) {
                outputStream.write((byte) ((encryptedValue >> (8 * i)) & 0xFF));
            }
        }
        return outputStream.toByteArray();
    }

    // combineEncryptedLongs的逆向函数
    @SneakyThrows
    private static List<Long> extractEncryptedLongs(byte[] data, int blockSize) {
        List<Long> encryptedLongs = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(data);

        // Ensure that the data length is a multiple of the blockSize
        if (data.length % blockSize != 0) {
            throw new IllegalArgumentException("Data length must be a multiple of the block size.");
        }

        while (buffer.hasRemaining()) {
            long encryptedValue = 0;
            for (int i = blockSize - 1; i >= 0; i--) {
                encryptedValue |= ((long) buffer.get() & 0xFF) << (8 * i);
            }
            encryptedLongs.add(encryptedValue);
        }
        return encryptedLongs;
    }

    public static long countPowAndMod(long base, long power, long mod) {
        // Math.pow会溢出，BigInteger
        BigInteger baseBig = BigInteger.valueOf(base);
        BigInteger powerBig = BigInteger.valueOf(power);
        BigInteger result = baseBig.pow(powerBig.intValue());
        BigInteger moduloValue = BigInteger.valueOf(mod);
        BigInteger remainder = result.mod(moduloValue);
        return remainder.longValue();
    }
}
