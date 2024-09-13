package garry.train.common.util;

import garry.train.common.consts.RedisConst;

/**
 * @author Garry
 * 2024-09-09 16:36
 */
public class RedisUtil {
    /**
     * 获取验证码的 redisKey
     */
    public static String getRedisKey4Code(String mobile) {
        return String.format(RedisConst.CODE_FORMAT, mobile);
    }
}
