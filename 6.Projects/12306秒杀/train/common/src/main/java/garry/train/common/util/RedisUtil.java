package garry.train.common.util;

import cn.hutool.core.date.DateUtil;
import garry.train.common.consts.RedisConst;

import java.util.Date;

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

    /**
     * 获取每日余票的 redisKey
     */
    public static String getRedisKey4DailyTicket(Date date, String trainCode, String start, String end) {
        return String.format(RedisConst.DAILY_TRAIN_TICKET_FORMAT, DateUtil.format(date, "yyyy-MM-dd"), trainCode, start, end);
    }
}
