package git.datsanvich.nada.fakedao;

import git.datsanvich.nada.model.MagicValue;
import git.datsanvich.nada.model.UserInformation;
import git.datsanvich.nada.util.ConversionUtil;
import okio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName UserDao
 * @Author GitDatSanvich
 * @Date 2020/1/20 9:29
 **/
@Component
public class UserDao {

    private static Logger logger = LoggerFactory.getLogger(UserDao.class);

    private String userAuthorityLocation;

    private Map<String, String> userInformationMap = new HashMap<>();

    private final Object FILE_LOCK = new Object();

    @PostConstruct
    public void initCheck() {
        String dir = "authority";
        String userAuthorityDir = System.getProperty("user.dir") + "\\" + dir;
        this.userAuthorityLocation = userAuthorityDir + "\\userAuthority.txt";

        logger.info("检查" + this.userAuthorityLocation + "是否存在");
        File userAuthorityFile = new File(userAuthorityLocation);
        //文件存在
        if (userAuthorityFile.exists()) {
            //读取文件
            readUserAuthorityFile();
        } else {
            File fileDir = new File(userAuthorityDir);
            synchronized (FILE_LOCK) {
                //创建文件夹
                if (!fileDir.exists()) {
                    logger.info("无文件夹 创建文件夹");
                    boolean dirFlag = fileDir.mkdir();
                    if (dirFlag) {
                        //创建文件
                        initFile();
                    } else {
                        logger.info("创建用户信息文件夹失败");
                    }
                }
                //文件夹存在文件不存在
                //创建文件
                initFile();
            }
        }
    }

    private void initFile() {
        logger.info("创建用户信息文件");
        File file = new File(userAuthorityLocation);
        if (!file.exists()) {
            logger.info("无文件 创建文件");
            try {
                boolean fileFlag = file.createNewFile();
                if (fileFlag) {
                    logger.info("创建用户信息文件成功");
                    logger.info("initCheck() --> success");
                } else {
                    logger.info("创建用户信息文件失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("创建用户信息文件失败");
            }
        }
    }

    public boolean insertUser(UserInformation userInformation) {
        this.checkRepetition(userInformation);
        this.userInformationMap.put(userInformation.getUserName(), ConversionUtil.objToJson(userInformation));
        try {
            rewriteUserAuthorityFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void checkRepetition(UserInformation userInformation) {
        Set<Map.Entry<String, String>> entries = userInformationMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            UserInformation userInformationOrg = ConversionUtil.jsonToObj(entry.getValue(), UserInformation.class);
            String userName = userInformationOrg.getUserName();
            if (userName.equals(userInformation.getUserName()) && userInformationOrg.getStatus().equals(MagicValue.STATUE_PASS)) {
                throw new RuntimeException("用户名已被注册");
            }
        }
    }

    private void rewriteUserAuthorityFile() {
        logger.info("写入文件上锁");
        synchronized (FILE_LOCK) {
            try {
                String writeJson = ConversionUtil.objToJson(userInformationMap);
                if (writeJson == null) {
                    logger.error("缓存转换Json失败!!");
                    return;
                }
                byte[] writeBytes = writeJson.getBytes();
                File rewriteKeyFile = new File(userAuthorityLocation);
                Sink sink = Okio.sink(rewriteKeyFile);
                BufferedSink bufferedSink = Okio.buffer(sink);
                bufferedSink.write(writeBytes);
                bufferedSink.flush();
                bufferedSink.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("添加失败");
            }
        }
        logger.info("写入文件解锁");
    }

    public UserInformation getUserInformationByUserId(String userName) {
        /*if (userInformationMap.containsKey(userName)) {
            return userInformationMap.get(userName);
        } else {
            readUserAuthorityFile();
            return userInformationMap.getOrDefault(userName, null);
        }*/
        return null;
    }

    private void readUserAuthorityFile() {
        logger.info("读取文件上锁");
        synchronized (FILE_LOCK) {
            try {
                File file = new File(userAuthorityLocation);
                Source source = Okio.source(file);
                BufferedSource buffer = Okio.buffer(source);
                byte[] bytes = buffer.readByteArray();
                String json = new String(bytes);
                this.userInformationMap = ConversionUtil.jsonToObj(json, HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("读取失败");
            }
        }
        logger.info("读取文件解锁");
    }

    public String login(String userName, String password) {
        Set<Map.Entry<String, String>> entries = userInformationMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            UserInformation value = ConversionUtil.jsonToObj(entry.getValue(), UserInformation.class);
            if (value.getUserName().equals(userName) &&
                    value.getPassWord().equals(password) &&
                    value.getStatus().equals(MagicValue.STATUE_PASS)) {
                return value.getUserName();
            } else if (value.getUserName().equals(userName) &&
                    value.getPassWord().equals(password) &&
                    value.getStatus().equals(MagicValue.STATUE_LOCK)) {
                return MagicValue.STATUE_LOCK;
            }
        }
        return null;
    }

    public void userActive(String userName) {
        Set<Map.Entry<String, String>> entries = userInformationMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            UserInformation value = ConversionUtil.jsonToObj(entry.getValue(), UserInformation.class);
            if (value.getUserName().equals(userName)) {
                value.setStatus(MagicValue.STATUE_PASS);
                userInformationMap.put(userName, ConversionUtil.objToJson(value));
                rewriteUserAuthorityFile();
            }
        }
    }
}