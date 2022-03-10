package beinet.cn.springmaildemo.mails;

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
public class QQMail implements MailOperator {
    @Value("${beinet.QQ.host:}")
    private String host;
    @Value("${beinet.QQ.protocal:imap}")
    private String protocol;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void folderBeforeOpen(Folder folder) {
    }
}
