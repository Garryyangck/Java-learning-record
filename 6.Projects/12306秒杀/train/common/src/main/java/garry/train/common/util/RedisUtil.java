package garry.train.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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
     * 如果参数为 null，则将其替换为 *
     */
    public static String getRedisKey4DailyTicket(Date date, String trainCode, String start, String end) {
        String dateStringFormat = "*";
        if (ObjectUtil.isNotNull(date)) {
            dateStringFormat = DateUtil.format(date, "yyyy-MM-dd");
        }
        trainCode = ObjectUtil.isNotNull(trainCode) ? trainCode : "*";
        start = ObjectUtil.isNotNull(start) ? start : "*";
        end = ObjectUtil.isNotNull(end) ? end : "*";
        return String.format(RedisConst.DAILY_TRAIN_TICKET_FORMAT, dateStringFormat, trainCode, start, end);
    }
}
