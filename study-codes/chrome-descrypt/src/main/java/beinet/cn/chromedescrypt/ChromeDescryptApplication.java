package beinet.cn.chromedescrypt;

import beinet.cn.chromedescrypt.chromeTest.ChromeUserDataReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChromeDescryptApplication {

    public static void main(String[] args) {
        readAndShowChromePassword();

        //SpringApplication.run(ChromeDescryptApplication.class, args);
    }

    /**
     * 读取并解密Chrome存储的账号密码
     */
    private static void readAndShowChromePassword() {
        /*
         * 账号密码存储的文件路径，在
         * "%LocalAppData%\Google\Chrome\User Data\Default\Login Data"
         * 找不到时，也可以在chrome里访问 chrome://version/ ，页面查找Profile Path
         * 这是一个sqlite数据库文件.
         * 注：建议关闭Chrome后再读取，也可以把它拷贝出来进行读取测试
         */
        String localPwdDB = "D:/Login Data";
        /*
         * 上面的密码是加密了的，解密的密码放在另一个文件里，文件路径在
         * %LocalAppData%\Google\Chrome\User Data\Local State"
         * 这是一个json格式的文件，密码在 os_crypt.encrypted_key 属性里.
         *
         * 注意：只能在本机运行该程序，解密其它机器上的文件会抛异常：
         * Exception in thread "main" com.sun.jna.platform.win32.Win32Exception: 该项不适于在指定状态下使用。
         */
        String localStateFile = "D:/Local State";
        var result = new ChromeUserDataReader(localPwdDB, localStateFile).queryData();
        for (var item : result) {
            // 输出解密后的账号密码
            System.out.println(item);
        }
    }

}
