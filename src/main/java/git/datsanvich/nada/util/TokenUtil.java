package git.datsanvich.nada.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @ClassName TokenUtil
 * @Author GitDatSanvich
 * @Date 2020/1/20 12:45
 **/

@Component
public class TokenUtil {

    private static Map<String, String> tokenMap = new HashMap<>(16);

    public String userLogin(String userName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        tokenMap.put(userName, uuid);
        Map<String, String> returnMap = new HashMap<>(16);
        returnMap.put(userName, uuid);
        return ConversionUtil.objToJson(returnMap);
    }

    public boolean checkLogin(String cookieValue) {
        Map<String, String> map = ConversionUtil.jsonToObj(cookieValue, HashMap.class);
        Set<Map.Entry<String, String>> entries = map.entrySet();
        if (entries.size() == 1) {
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (tokenMap.containsKey(key)) {
                    return tokenMap.get(key).equals(value);
                } else {
                    throw new RuntimeException("Cookie无效请重新登陆");
                }
            }
        } else {
            throw new RuntimeException("Cookie有误请重新登陆");
        }
        throw new RuntimeException("未知错误!");
    }
}
