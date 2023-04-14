package beinet.cn.algorithmdemo;

import beinet.cn.algorithmdemo.encrypts.AesUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 新类
 *
 * @author youbl
 * @date 2023/4/14 11:47
 */
@Component
public class AesDemoStartup implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String source = "beinet.cn-youbl@126.com";
        String key = "beinet.cn";
        String iv = "salt.beinet.cn";

        String encStr = AesUtils.encrypt(source, key, iv);
        System.out.println("加密结果-不变： " + encStr);
        System.out.println(AesUtils.decrypt(encStr, key, iv));
        System.out.println();

        encStr = AesUtils.encryptRndIV(source, key);
        System.out.println("加密结果-变化： " + encStr);
        System.out.println(AesUtils.decryptRndIV(encStr, key));
        Thread.sleep(1000);

        encStr = AesUtils.encrypt(source, key, iv);
        System.out.println("加密结果-不变： " + encStr);
        System.out.println(AesUtils.decrypt(encStr, key, iv));
        System.out.println();

        encStr = AesUtils.encryptRndIV(source, key);
        System.out.println("加密结果-变化： " + encStr);
        System.out.println(AesUtils.decryptRndIV(encStr, key));
        Thread.sleep(1000);
    }


}
