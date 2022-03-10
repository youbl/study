package beinet.cn.springmaildemo.mails;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/10 17:51
 */
@Data
@Builder
public class MailDto {
    // 邮件ID
    private String messageId;
    // 发送时间
    private String sendDate;
    // 发件人
    private String from;
    // 收件人
    private String to;
    // 抄送人
    private String cc;
    // 暗送人
    private String bcc;
    // 邮件主题
    private String subject;
    // 邮件正文
    private String content;

    /*
System.out.println("邮件【" + i + "】是否需要回复:" + re.getReplySign());
System.out.println("邮件【" + i + "】是否已读:" + re.isNew());
System.out.println("邮件【" + i + "】是否包含附件:" + re.isContainAttach(message));
    * */
}
