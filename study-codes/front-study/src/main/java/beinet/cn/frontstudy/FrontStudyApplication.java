package beinet.cn.frontstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
//@RefreshScope
public class FrontStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        testStrEquals();
        testNumEquals();

        SpringApplication.run(FrontStudyApplication.class, args);
    }

    private static void testStrEquals() {
        String str1 = "abcdefg";
        String str2 = "abcdefg";
        String str3 = new String("abcdefg");
        String str4 = new StringBuilder().append("abcdefg").toString();
        System.out.println(str1 == str2);           // true
        System.out.println(str1 == str3);           // false
        System.out.println(str1 == str4);           // false
        System.out.println(str1.equals(str2));      // true
        System.out.println(str1.equals(str3));      // true
        System.out.println(str1.equals(str4));      // true

        System.out.println(str3 == str4);           // false
        System.out.println(str3.equals(str4));      // true
    }

    private static void testNumEquals() {
        Long aa = 88888L;
        long bb = 88888;
        int cc = 88888;
        Integer dd = 88888;

        System.out.println(aa.equals(bb));              // true
        System.out.println(aa == bb);                   // true

        System.out.println(aa.equals(cc));              // false
        System.out.println(aa == cc);                   // true

        System.out.println(aa.equals(dd));              // false
        System.out.println(aa == Long.valueOf(dd));     // false
        System.out.println(Math.toIntExact(aa) == dd);  // true

        System.out.println(bb == cc);                   // true
        System.out.println(bb == Long.valueOf(cc));     // true
        System.out.println(Math.toIntExact(bb) == cc);  // true

        System.out.println(dd.equals(bb));              // false
        System.out.println(dd == bb);                   // true

        System.out.println(dd.equals(cc));              // true
        System.out.println(dd == cc);                   // true
    }

    @Autowired
    Environment env;

//    @Autowired
//    ConfigTest1 configTest1;
//
//    @Autowired
//    ConfigTest2 configTest2;

    @Value("${beinet.newVal:}")
    String str3;

    // 调用 curl -X POST http://localhost:8801/actuator/refresh
    // 刷新后，这些配置是否会变化呢？
    @Override
    public void run(String... args) throws Exception {
//        while (true) {
//            //String str = env.getProperty("beinet.config");
//            String str2 = env.getProperty("beinet.newVal");
//            System.out.println(configTest1.getStr3()); // 没加RefreshScope注解，不会刷新
//            System.out.println(configTest2.getStr3()); // 会刷新
//            System.out.println(str2 + "--");           // 通过Env获取，会刷新
//            System.out.println(str3 + "--");           // main所在类的RefreshScope不生产，不会刷新
//            Thread.sleep(10000);
//        }
    }
}
