package garry.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author Garry
 * ---------2024/3/17 10:11
 **/

/**
 * Community项目中的通用方法类
 */
public class CommunityUtil {

    /**
     * 使用UUID生成随机字符串，长度为32位
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString()
                .replaceAll("-", "");//将所有"-"删除
    }

    /**
     * 使用md5加密添加salt后的密码，加密密码32位
     *
     * @param key
     * @return
     */
    public static String md5Encryption(String key) {
        if (StringUtils/*org.apache.commons.lang3.StringUtils*/.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
