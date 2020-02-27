package git.datsanvich.nada.service.Impl;

import git.datsanvich.nada.fakedao.UserDao;
import git.datsanvich.nada.mail.MailContentTypeEnum;
import git.datsanvich.nada.mail.MailKey;
import git.datsanvich.nada.mail.MailSender;
import git.datsanvich.nada.model.UserInformation;
import git.datsanvich.nada.service.UserService;
import git.datsanvich.nada.util.Md5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName IndexServiceImpl
 * @Author GitDatSanvich
 * @Date 2020/1/20 11:04
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private MailSender mailSender;

    @Resource
    private MailKey mailKey;

    @Override
    public String login(String userName, String password) {
        //密码MD5处理
        String md5Password = Md5Util.md5(password);
        return userDao.login(userName, md5Password);
    }

    @Override
    public boolean loginCheck(String userId) {
        UserInformation userInformationByUserId = userDao.getUserInformationByUserId(userId);
        return userInformationByUserId != null;
    }

    @Override
    public boolean signIn(UserInformation userInformation) {
        //封装用户
        String userName = userInformation.getUserName();
        Date createDate = new Date();
        userInformation.setCreateDate(createDate);
        userInformation.setStatus("1");
        userInformation.setUpdateDate(createDate);
        //密码加密
        userInformation.setPassWord(Md5Util.md5(userInformation.getPassWord()));
        //存入用户
        boolean flag = userDao.insertUser(userInformation);
        //生成邮箱激活key
        String key = mailKey.initKey(userName);
        //发送邮箱
        if (flag) {
            //邮件发送
            try {
                mailSender.title("这是一封激活邮件")
                        .contentType(MailContentTypeEnum.HTML)
                        .targets(new ArrayList<String>() {{
                            add(userInformation.getEmail());
                        }}).content("<h3 style=\"width: 100%; text-align: center\">亲爱的" + userInformation.getUserName() + "您好,感谢您使用三明治慢传.</h3>\n" +
                        "<h3 style=\"width: 100%; text-align: center\">您的账号需要点击下面的链接进行激活.</h3>\n" +
                        "<h3 style=\"width: 100%; text-align: center\">激活后即可使用此→→→<公共>←←←网盘.</h3>\n" +
                        "<h3 style=\"width: 100%; text-align: center\">如果您有问题,有疑问,您可以随时通过这个邮件回复我</h3>\n" +
                        "<h3 style=\"width: 100%; text-align: center\">激活链接的有效期再下次奇数天结束</h3>\n" +
                        "<div style=\"width: 100%; text-align: center\">\n" +
                        "    <h3><a href=" +
                        //TODO 上线时改成服务器IP
                        "\"http://localhost:50500/user/userActive?userName=" + userName + "&key=" + key + "\"" +
                        ">激活!</a></h3>\n" +
                        "<h3 style=\"width: 100%; text-align: center\">如果无法激活请复制下方链接至浏览器!</h3>\n" +
                        "<h3 style=\"width: 100%; text-align: center\">http://localhost:50500/user/userActive?userName=" + userName + "&key=" + key + "</h3>\n" +
                        "</div>").send();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("邮件发送失败");
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String userActive(String userName, String key) {
        try {
            if (mailKey.checkKey(userName, key)) {
                userDao.userActive(userName);
                //TODO 上线时改成服务器IP
                return "SUCCESS  去登陆!    <h3><a href=" +
                        "http://localhost:50500/index.html" +
                        ">走着!</a></h3>\n";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
