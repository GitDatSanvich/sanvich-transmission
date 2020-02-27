package git.datsanvich.nada.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName MailKey
 * @Author GitDatSanvich
 * @Date 2020/1/21 14:22
 **/
@Component
public class MailKey {

    public static final Logger logger = LoggerFactory.getLogger(MailKey.class);

    private Map<String, String> keyMap = new HashMap<>(16);


    public String initKey(String userName) {
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        keyMap.put(userName, key);
        return key;
    }

    public boolean checkKey(String userName, String key) {
        if (keyMap.containsKey(userName)) {
            return keyMap.get(userName).equals(key);
        } else {
            return false;
        }
    }

    @Scheduled(cron = "0 0 0 1/2 * ? ")
    private void cleanKey() {
        logger.info("清理邮箱Key");
        this.keyMap = new HashMap<>(16);
    }
}
