package git.datsanvich.nada.model;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName UserInformation
 * @Author GitDatSanvich
 * @Date 2020/1/20 10:14
 **/

public class UserInformation {

    Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private String userName;
    private String passWord;
    private String email;
    private String status;
    private Date createDate;
    private Date lastLogin;
    private Date updateDate;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public UserInformation(String userName, String passWord, String email, String status, Date createDate, Date lastLogin, Date updateDate) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.status = status;
        this.createDate = createDate;
        this.lastLogin = lastLogin;
        this.updateDate = updateDate;
    }

    public UserInformation() {
    }

    public boolean checkNull() {
        if (this.getPassWord() == null || "".equals(this.getPassWord().trim())) {
            return false;
        }
        if (this.getUserName() == null || "".equals(this.getUserName().trim())) {
            return false;
        }
        if (this.getEmail() == null || "".equals(this.getEmail().trim())) {
            return false;
        }
        Matcher matcher = emailPattern.matcher(this.getEmail().trim());
        if (!matcher.matches()) {
            return false;
        }
        this.setPassWord(this.getPassWord().trim());
        this.setUserName(this.getUserName().trim());
        this.setEmail(this.getEmail().trim());
        return true;
    }
}
