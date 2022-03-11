package beinet.cn.springmaildemo.mails;

import com.sun.mail.imap.IMAPFolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Folder;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/10 17:58
 */
@Component
public class Netease163Mail extends MailOperator {
    @Value("${beinet.163.host:}")
    private String host;
    @Value("${beinet.163.protocal:imap}")
    private String protocol;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    /*
    163邮箱，不执行这段代码会抛异常：
    javax.mail.MessagingException: A3 NO SELECT Unsafe Login. Please contact kefu@188.com for help;
  nested exception is:
	com.sun.mail.iap.CommandFailedException: A3 NO SELECT Unsafe Login. Please contact kefu@188.com for help
	at com.sun.mail.imap.IMAPFolder.open(IMAPFolder.java:1049)
	at com.sun.mail.imap.IMAPFolder.open(IMAPFolder.java:977)
    * */
    @Override
    public void folderBeforeOpen(Folder folder) {
        if (folder instanceof IMAPFolder) {
            IMAPFolder imapFolder = (IMAPFolder) folder;
            //javamail中使用id命令有校验checkOpened, 所以要去掉id方法中的checkOpened();
            try {
                imapFolder.doCommand(p -> {
                    p.id("FUTONG");
                    return null;
                });
            } catch (Exception exp) {
                throw new RuntimeException(exp);
            }
        }
    }
}
