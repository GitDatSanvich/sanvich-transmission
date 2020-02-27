package git.datsanvich.nada.service;

import git.datsanvich.nada.model.UserInformation;

/**
 * @ClassName IndexService
 * @Author GitDatSanvich
 * @Date 2020/1/20 11:04
 **/
public interface UserService {
    /**
     * 用户登陆
     *
     * @param userName userName
     * @param password password
     * @return userId
     */
    String login(String userName, String password);

    /**
     * 确认是否登陆
     * H
     *
     * @param userId userId
     * @return return
     */
    boolean loginCheck(String userId);

    /**
     * 用户注册
     *
     * @param userInformation userInformation
     * @return flag
     */
    boolean signIn(UserInformation userInformation);

    /**
     * 用户激活
     *
     * @param userName userName
     * @param key      key
     * @return tips
     */
    String userActive(String userName, String key);
}
