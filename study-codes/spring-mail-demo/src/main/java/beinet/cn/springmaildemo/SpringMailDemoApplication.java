package beinet.cn.springmaildemo;

import beinet.cn.springmaildemo.mails.MailDto;
import beinet.cn.springmaildemo.mails.MailProvider;
import beinet.cn.springmaildemo.mails.MailProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class SpringMailDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        //recieveOutlookMail();
        //sendOutlookMail();
        SpringApplication.run(SpringMailDemoApplication.class, args);
    }

    @Autowired
    MailProviderFactory mailProviderFactory;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run(String... args) throws Exception {
        String username = "abc@163.com";
        // 163和QQ，都要用授权码，而不是登录密码；outlook使用登录密码
        String password = "123456";

        MailProvider provider = mailProviderFactory.getProvider(username);
        if (provider == null) {
            throw new RuntimeException("未找到邮箱提供者");
        }
        List<MailDto> mails = readMail(provider, username, password);
        checkAllMail(mails);
    }

    private List<MailDto> readMail(MailProvider provider, String username, String password) {
        try {
            // 读取邮件
            List<MailDto> mailList = provider.getMailByDate(username, password, sdf.parse("2022-03-01"));

            // 成功，输出
            // System.out.println(mailList);
            return mailList;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAllMail(List<MailDto> mailList) {
        for (MailDto dto : mailList) {
            checkMail(dto);
        }
    }

    private void checkMail(MailDto mail) {
        String content = mail.getContent();

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


    static void recieveOutlookMail() {
        try {
            String username = "abc@outlook.com";
            // 登录密码
            String password = "123456";
            Properties properties = new Properties();
//            properties.put("mail.imap.host", "outlook.office365.com");
//            properties.put("mail.imap.port", "995");
//            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.debug", "false");
            Session emailSession = Session.getDefaultInstance(properties);

            try (Store store = emailSession.getStore("imaps")) {
                store.connect("outlook.office365.com", username, password);

                try (Folder emailFolder = store.getFolder("Inbox")) {
                    emailFolder.open(Folder.READ_WRITE);

                    Message[] messages = emailFolder.getMessages();//.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                    System.out.println("messages.length---" + messages.length);
                    if (messages.length == 0) {
                        System.out.println("No new messages found.");
                    } else {
                        for (int i = 0, len = messages.length; i < len; i++) {
                            Message message = messages[i];

                            System.out.println("Email #" + (i + 1) + " subject: " + message.getSubject());

                            Folder copyFolder = store.getFolder("copyData");
                            if (copyFolder.exists()) {
                                System.out.println("copy messages...");
                                copyFolder.copyMessages(messages, emailFolder);
                                message.setFlag(Flags.Flag.DELETED, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 报错： unknown user name or bad password.
    // google发现:很多人在微软和GMail上提问，未解决
    static void recieveOutlookMail2() {
        try {
            String username = "abc@outlook.com";
            // 登录密码
            String password = "123456";

            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3s");
            properties.put("mail.pop3s.host", "outlook.office365.com");
            properties.put("mail.pop3s.port", "995");
            properties.put("mail.pop3s.auth", "true");
            properties.put("mail.pop3s.starttls.enable", "true");
            properties.put("mail.pop3s.connectiontimeout", 5000);
            properties.put("mail.pop3s.timeout", 5000);
//            properties.put("mail.pop3s.partialfetch", false);
//            properties.put("mail.pop3s.auth.mechanisms", "XOAUTH2");
//            properties.put("mail.pop3s.auth.xoauth2.two.line.authentication.format", "true");
//
//            properties.put("mail.pop3.sasl.enable", "true");
//            properties.put("mail.pop3.sasl.mechanisms", "XOAUTH2");
//            properties.put("mail.pop3.auth.login.disable", "true");
//            properties.put("mail.pop3.auth.plain.disable", "true");
//            properties.put("mail.pop3s.auth.plain.disable", "true");
            properties.put("mail.debug", "true");

            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            //session.setDebug(false);

            Store store = session.getStore("pop3s");
            store.connect("outlook.office365.com", username, password);
            try (Folder folder = store.getFolder("Inbox")) {
                folder.open(Folder.READ_ONLY);

                Message[] messages = folder.getMessages();
                System.out.println(messages);
            }
            System.out.println("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
            //System.out.println("出错了:" + e.toString());
        }
    }

    // 使用outlook发邮件成功的代码
    static void sendOutlookMail() {
        try {
            String username = "abc@outlook.com";
            // 登录密码
            String password = "123456";

            String to = "youbl@126.com";
            String from = username;

            Properties props = new Properties();
//            props.put("mail.smtp.socketFactory.port", "587");
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "true");
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

//            Session emailSession = Session.getDefaultInstance(props, null);

            String msgBody = "Sending email using JavaMail APIaaa...";

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "NoReply"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to, "Mr. Recipient"));
            msg.setSubject("Welcome To Java Mail APIbbbb");
            msg.setText(msgBody);
            Transport.send(msg);
            System.out.println("Email sent successfully...");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}
