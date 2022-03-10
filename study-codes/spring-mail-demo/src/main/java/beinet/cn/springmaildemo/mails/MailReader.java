package beinet.cn.springmaildemo.mails;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.ObjectUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 邮件读取类
 */
public class MailReader {

    public final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final MimeMessage mimeMessage;

    public MailReader(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public MailDto getDto() {
        try {
            return MailDto.builder()
                    .messageId(mimeMessage.getMessageID())
                    .sendDate(getSentDate())
                    .from(getFrom())
                    .to(getMailAddress("TO"))
                    .cc(getMailAddress("CC"))
                    .bcc(getMailAddress("BCC"))
                    .subject(getSubject())
                    .content(getMailContent(mimeMessage))
                    .build();
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    // 获得发件人的地址和姓名
    private String getFrom() throws MessagingException, UnsupportedEncodingException {
        InternetAddress[] address = (InternetAddress[]) mimeMessage.getFrom();
        if (address.length < 1)
            return "";
        return getMailAddr(address[0]);
    }

    // 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同
    // type "to"----收件人　"cc"---抄送人地址　"bcc"---密送人地址
    private String getMailAddress(String type) throws MessagingException, UnsupportedEncodingException {
        if (ObjectUtils.isEmpty(type)) {
            return "";
        }
        InternetAddress[] address;
        if (type.equalsIgnoreCase("TO")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
        } else if (type.equalsIgnoreCase("CC")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
        } else {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
        }

        if (ObjectUtils.isEmpty(address)) {
            return "";
        }
        StringBuilder mailAddr = new StringBuilder();
        for (InternetAddress internetAddress : address) {
            if (mailAddr.length() > 0)
                mailAddr.append(",");
            String emailAddr = getMailAddr(internetAddress);
            mailAddr.append(emailAddr);
        }
        return mailAddr.toString();
    }

    private String getMailAddr(InternetAddress address) throws UnsupportedEncodingException {
        String from = address.getAddress();
        if (from == null) {
            from = "";
        }
        String personal = address.getPersonal();
        if (personal == null) {
            personal = "";
        }

        String mailAddr = personal.isEmpty() ? "" : MimeUtility.decodeText(personal);
        if (!from.isEmpty()) {
            mailAddr += "<" + MimeUtility.decodeText(from) + ">";
        }
        return mailAddr;
    }

    // 获得邮件主题
    private String getSubject() throws MessagingException, UnsupportedEncodingException {
        String subject = MimeUtility.decodeText(mimeMessage.getSubject());
        if (subject == null) {
            subject = "";
        }
        return subject;
    }

    // 获得邮件发送日期
    private String getSentDate() throws MessagingException {
        Date sentDate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_PATTERN);
        return format.format(sentDate);
    }

    // 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件
    // 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
    public String getMailContent(Part part) throws MessagingException, IOException {
        StringBuilder bodyText = new StringBuilder();
        String contentType = part.getContentType();
        int nameIndex = contentType.indexOf("name");
        boolean conName = nameIndex != -1;

        if (part.isMimeType("text/plain") && !conName) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conName) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                bodyText.append(this.getMailContent(multipart.getBodyPart(i)));
            }
        } else if (part.isMimeType("message/rfc822")) {
            bodyText.append(this.getMailContent((Part) part.getContent()));
        }
        return bodyText.toString();
    }

    // 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
    private boolean getReplySign() throws MessagingException {
        boolean replySign = false;

        String[] needReply = mimeMessage.getHeader("Disposition-Notification-To");
        if (needReply != null) {
            replySign = true;
        }
        return replySign;
    }

    // 判断此邮件是否已读，如果未读返回false,反之返回true
    private boolean isNew() throws MessagingException {
        Flags flags = mimeMessage.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (Flags.Flag value : flag) {
            if (value == Flags.Flag.SEEN) {
                return true;
            }
        }
        return false;
    }

    // 判断此邮件是否包含附件
    private boolean isContainAttach(Part part) throws MessagingException, IOException {
        boolean attachFlag = false;
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            BodyPart mPart;
            String conType;
            for (int i = 0; i < mp.getCount(); i++) {
                mPart = mp.getBodyPart(i);
                String disposition = mPart.getDisposition();
                if (Part.ATTACHMENT.equals(disposition) || Part.INLINE.equals(disposition)) {
                    attachFlag = true;
                } else if (mPart.isMimeType("multipart/*")) {
                    attachFlag = this.isContainAttach(mPart);
                } else {
                    conType = mPart.getContentType();
                    if (conType.toLowerCase().contains("application") || conType.toLowerCase().contains("name")) {
                        attachFlag = true;
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachFlag = isContainAttach((Part) part.getContent());
        }
        return attachFlag;
    }

    // 保存附件
    private void saveAttachMent(Part part) throws MessagingException, IOException {
        String fileName;
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            BodyPart mPart;
            for (int i = 0; i < mp.getCount(); i++) {
                mPart = mp.getBodyPart(i);
                String disposition = mPart.getDisposition();
                if (Part.ATTACHMENT.equals(disposition) || Part.INLINE.equals(disposition)) {
                    fileName = mPart.getFileName();
                    if (null != fileName && fileName.toLowerCase().contains("gb2312")) {
                        fileName = MimeUtility.decodeText(fileName);
                    }
                    this.saveFile(fileName, mPart.getInputStream());
                } else if (mPart.isMimeType("multipart/*")) {
                    this.saveAttachMent(mPart);
                } else {
                    fileName = mPart.getFileName();
                    if ((fileName != null) && (fileName.toLowerCase().contains("gb2312"))) {
                        fileName = MimeUtility.decodeText(fileName);
                        this.saveFile(fileName, mPart.getInputStream());
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            this.saveAttachMent((Part) part.getContent());
        }
    }

    // 真正的保存附件到指定目录里
    private void saveFile(String fileName, InputStream in) throws IOException {
        String osName = System.getProperty("os.name");
        String storeDir = "D:\\";
        if (null == osName) {
            osName = "";
        }
        if (osName.toLowerCase().contains("win")) {
            if (ObjectUtils.isEmpty(storeDir))
                storeDir = "C:\\tmp";
        } else {
            storeDir = "/tmp";
        }
//        fileName=fileName.replace("=?", "");
//        fileName=fileName.replace("?=", "");
//        fileName = fileName.substring(fileName.length() - 6, fileName.length());
        try (FileOutputStream fos = new FileOutputStream(storeDir + File.separator + fileName)) {
            IOUtils.copy(in, fos);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(in);
        }
    }

}