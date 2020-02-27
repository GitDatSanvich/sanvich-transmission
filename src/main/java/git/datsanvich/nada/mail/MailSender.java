package git.datsanvich.nada.mail;


import git.datsanvich.nada.util.PropertiesUtil;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName MailSender
 * @Author GitDatSanvich
 * @Date 2019/5/29 9:42
 **/
@Component
public class MailSender {
    /**
     * 邮件实体
     */
    private static MailEntity mail = new MailEntity();

    /**
     * 设置邮件标题
     *
     * @param title 标题信息
     * @return MailSender
     */
    public MailSender title(String title) {
        mail.setTitle(title);
        return this;
    }

    /**
     * 设置邮件内容
     *
     * @param content content
     * @return MailSender
     */
    public MailSender content(String content) {
        mail.setContent(content);
        return this;
    }

    /**
     * 设置邮件格式
     *
     * @param typeEnum typeEnum
     * @return MailSender
     */
    public MailSender contentType(MailContentTypeEnum typeEnum) {
        mail.setContentType(typeEnum.getValue());
        return this;
    }

    /**
     * 设置请求目标邮件地址
     *
     * @param targets targets
     * @return MailSender
     */
    public MailSender targets(List<String> targets) {
        mail.setList(targets);
        return this;
    }

    /**
     * 执行发送邮件
     *
     * @throws Exception 如果发送失败会抛出异常信息
     */
    public void send() throws Exception {
        //默认使用html内容发送
        if (mail.getContentType() == null) {
            mail.setContentType(MailContentTypeEnum.HTML.getValue());
        }

        if (mail.getTitle() == null || mail.getTitle().trim().length() == 0) {
            throw new Exception("邮件标题没有设置.调用title方法设置");
        }

        if (mail.getContent() == null || mail.getContent().trim().length() == 0) {
            throw new Exception("邮件内容没有设置.调用content方法设置");
        }

        if (mail.getList().size() == 0) {
            throw new Exception("没有接受者邮箱地址.调用targets方法设置");
        }

        // 创建Properties 类用于记录邮箱的一些属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.qq.com");
        //设置端口号，QQ邮箱给出了两个端口465/587
        props.put("mail.smtp.port", "587");
        // 设置发送邮箱
        props.put("mail.user", "704321764@qq.com");
        // 设置发送邮箱的16位STMP口令
        props.put("mail.password", "nfkfyehtwqgvbdac");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        String nickName = MimeUtility.encodeText("admin");
        InternetAddress form = new InternetAddress(nickName + " <" + props.getProperty("mail.user") + ">");
        message.setFrom(form);

        // 设置邮件标题
        message.setSubject(mail.getTitle());
        //html发送邮件
        if (mail.getContentType().equals(MailContentTypeEnum.HTML.getValue())) {
            // 设置邮件的内容体
            message.setContent(mail.getContent(), mail.getContentType());
        }
        //文本发送邮件
        else if (mail.getContentType().equals(MailContentTypeEnum.TEXT.getValue())) {
            message.setText(mail.getContent());
        }
        //发送邮箱地址
        List<String> targets = mail.getList();
        for (String target : targets) {
            try {
                // 设置收件人的邮箱
                InternetAddress to = new InternetAddress(target);
                message.setRecipient(Message.RecipientType.TO, to);
                // 最后当然就是发送邮件啦
                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
