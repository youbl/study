package beinet.cn.chromedescrypt;

import beinet.cn.chromedescrypt.chromeTest.ChromeUserDataReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChromeDescryptApplication {

    public static void main(String[] args) {
        String localStateFile = "D:/Local State"; // 解密密钥文件路径
        String localPwdDB = "D:/Login Data";      // 账号密码的sqlite文件路径
        var result = new ChromeUserDataReader(localPwdDB, localStateFile).queryData();
        for (var item : result) {
            System.out.println(item);
        }
        //SpringApplication.run(ChromeDescryptApplication.class, args);
    }

}
