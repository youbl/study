package beinet.cn.springmaildemo.mails;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.search.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/10 17:50
 */
public abstract class MailOperator {

    /**
     * 读取所有邮件
     *
     * @param username 用户名
     * @param password 密码
     * @return 邮件列表
     */
    public List<MailDto> getAllMail(String username, String password) {
        return readMailFromFolder(username, password, folder -> {
            try {
                return folder.getMessages();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 读取指定日期之后的所有邮件
     *
     * @param username  用户名
     * @param password  密码
     * @param beginDate 指定日期
     * @return 邮件列表
     */
    public List<MailDto> getMailByDate(String username, String password, Date beginDate) {
        return getMailByDate(username, password, beginDate, new Date());
    }

    /**
     * 读取指定日期之间的所有邮件
     *
     * @param username  用户名
     * @param password  密码
     * @param beginDate 指定起始日期
     * @param endDate   指定结束日期
     * @return 邮件列表
     */
    public List<MailDto> getMailByDate(String username, String password, Date beginDate, Date endDate) {
        return readMailFromFolder(username, password, folder -> {
            try {
                // 注 网易邮箱使用 ReceivedDateTerm搜索无效
                SearchTerm beginTerm = new SentDateTerm(ComparisonTerm.GE, beginDate);
                SearchTerm endTerm = new SentDateTerm(ComparisonTerm.LE, endDate);
                SearchTerm searchTerm = new AndTerm(beginTerm, endTerm);
                return folder.search(searchTerm);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 读取未读邮件
     * 注意：仅imap协议支持，pop3协议不支持，仅返回最近10封
     *
     * @param username 用户名
     * @param password 密码
     * @return 邮件列表
     */
    public List<MailDto> getUnreadMail(String username, String password) {
        return readMailFromFolder(username, password, folder -> {
            try {
                int allNum = folder.getMessageCount();
                System.out.println("全部邮件数:" + allNum);
                int unreadNum = folder.getUnreadMessageCount();
                System.out.println("未读邮件数:" + unreadNum);
                if (getProtocol().equalsIgnoreCase("imap")) {
                    // 搜索未读邮件，只有imap支持，false代表未读，true代表已读。 pop3协议不支持
                    FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
                    return folder.search(ft);
                    // return folder.getMessages(allNum - unreadNum, allNum);
                }

                // pop3取最后10封
                if (allNum <= 10)
                    return folder.getMessages();
                return folder.getMessages(allNum - 10, allNum);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<MailDto> readMailFromFolder(String username, String password, FolderOperator operator) {
        try {
            String protocol = getProtocol();
            Properties props = new Properties();// System.getProperties();
            props.put("mail.store.protocol", protocol);
            //创建会话
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(false);
            //存储对象
            try (Store store = session.getStore(protocol)) {
                //连接
                store.connect(getHost(), username, password);
                //创建目录对象
                try (Folder folder = store.getFolder("Inbox")) {
                    if (folder == null) {
                        return new ArrayList<>();
                    }

                    folderBeforeOpen(folder);
                    folder.open(Folder.READ_ONLY);
                    // List<MailDto> ret1 = convertMails(folder.getMessages());
                    // List<MailDto> ret2 = convertMails(folder.getMessages());
                    // List<MailDto> ret3 = convertMails(folder.getMessages());

                    Message[] messages = operator.getMessages(folder);
                    return convertMails(messages);
                }
            }
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private List<MailDto> convertMails(Message[] messages) {
        List<MailDto> ret = new ArrayList<>(messages.length);
        for (Message message : messages) {
            MailDto item = new MailReader((MimeMessage) message).getDto();
            ret.add(item);
        }
        return ret;
    }

    public abstract String getHost();

    public abstract String getProtocol();

    // 读取Inbox等邮箱内容前的操作项，比如163必须要doCommand
    protected abstract void folderBeforeOpen(Folder folder);

    private interface FolderOperator {
        Message[] getMessages(Folder folder);
    }
}
