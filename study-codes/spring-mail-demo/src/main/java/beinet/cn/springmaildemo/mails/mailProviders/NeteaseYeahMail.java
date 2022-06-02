package beinet.cn.springmaildemo.mails.mailProviders;

import beinet.cn.springmaildemo.mails.MailProvider;
import com.sun.mail.imap.IMAPFolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.Folder;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/6/2 17:58
 */
@Component
public class NeteaseYeahMail extends MailProvider {
    @Value("${beinet.yeah.host:imap.yeah.net}")
    private String host;
    @Value("${beinet.yeah.protocal:imap}")
    private String protocol;

    @Override
    public boolean match(String mailAddress) {
        return (StringUtils.hasLength(mailAddress)) && mailAddress.toLowerCase().endsWith("@yeah.net");
    }
    
    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    /*
    163邮箱使用POP3协议无影响，使用IMAP协议时，不执行这段代码会抛异常：
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
