package beinet.cn.springmaildemo;

import beinet.cn.springmaildemo.mails.MailDto;
import beinet.cn.springmaildemo.mails.Netease163Mail;
import beinet.cn.springmaildemo.mails.QQMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class SpringMailDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringMailDemoApplication.class, args);
    }

    @Autowired
    Netease163Mail netease163;

    @Autowired
    QQMail qqMail;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run(String... args) throws Exception {
        List<MailDto> mail163 = read163Mail();
        checkMail(mail163);

//        List<MailDto> mailQQ = readQQMail();
//        checkMail(mailQQ);
    }

    private List<MailDto> read163Mail() {
        String username = "abc@163.com";
        // 授权码，而不是登录密码
        String password = "12345";

        try {
            // 读取邮件
            List<MailDto> mailList = netease163.getMailByDate(username, password, sdf.parse("2022-03-01"));

            // 成功，输出
            // System.out.println(mailList);
            return mailList;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MailDto> readQQMail() {
        String username = "12345@qq.com";
        // 授权码，而不是登录密码
        String password = "abcde";

        try { // 读取邮件
            List<MailDto> mailList = qqMail.getMailByDate(username, password, sdf.parse("2022-03-01"));
            // 成功，输出
            // System.out.println(mailList);
            return mailList;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkMail(List<MailDto> mailList) {
        // 最后一封邮件的内容分析
        String content = mailList.get(mailList.size() - 1).getContent();

        // 清除样式 和 html标签、空白
        String noHtmlContent = content.replaceAll("<style[^>]*>[\\s\\S]*</style>", "")
                .replaceAll("<[^>]*>", "")
                .replaceAll("\\s", "");
        System.out.println(noHtmlContent);

        // 匹配出Shopee的验证码
        Pattern pattern = Pattern.compile("[^\\d](\\d{6})[^\\d].*?Shopee");

        Matcher matcher = pattern.matcher(noHtmlContent);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
