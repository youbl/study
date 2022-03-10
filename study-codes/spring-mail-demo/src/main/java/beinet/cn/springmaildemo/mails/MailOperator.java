package beinet.cn.springmaildemo.mails;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/10 17:50
 */
public interface MailOperator {
    default List<MailDto> getMail(String username, String password) {
        try {
            String protocol = getProtocol();
            Properties props = new Properties();// System.getProperties();
            props.put("mail.store.protocol", protocol);
            //创建会话
            Session session = Session.getDefaultInstance(props, null);
            //存储对象
            try (Store store = session.getStore(protocol)) {
                //连接
                store.connect(getHost(), username, password);
                //创建目录对象
                Folder folder = store.getFolder("Inbox");

                if (folder == null) {
                    return new ArrayList<>();
                }

                folderBeforeOpen(folder);
                folder.open(Folder.READ_WRITE);

                Message[] messages = folder.getMessages();
                return readMails(messages);
            }
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    default List<MailDto> readMails(Message[] messages) {
        List<MailDto> ret = new ArrayList<>(messages.length);
        for (Message message : messages) {
            MailDto item = new MailReader((MimeMessage) message).getDto();
            ret.add(item);
        }
        return ret;
    }

    String getHost();

    String getProtocol();

    // 读取Inbox等邮箱内容前的操作项，比如163必须要doCommand
    void folderBeforeOpen(Folder folder);

}
