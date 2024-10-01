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
     * 如果参数为 null，则将其替换为 *
     */
    public static String getRedisKey4Code(String mobile) {
        mobile = checkString(mobile);
        return String.format(RedisConst.CODE_FORMAT, mobile);
    }

    /**
     * 获取每日余票的 redisKey
     * 如果参数为 null，则将其替换为 *
     */
    public static String getRedisKey4DailyTicket(Date date, String trainCode, String start, String end) {
        String dateStringFormat = checkDate(date, "yyyy-MM-dd");
        trainCode = checkString(trainCode);
        start = checkString(start);
        end = checkString(end);
        return String.format(RedisConst.DAILY_TRAIN_TICKET_FORMAT, dateStringFormat, trainCode, start, end);
    }

    /**
     * 将 date 转换为 formatString
     * date 若为空，则转化为 *
     */
    private static String checkDate(Date date, String format) {
        String dateStringFormat = "*";
        if (ObjectUtil.isNotNull(date)) {
            dateStringFormat = DateUtil.format(date, format);
        }
        return dateStringFormat;
    }

    /**
     * param 若为空，则转化为 *
     */
    private static String checkString(String param) {
        return ObjectUtil.isNotNull(param) ? param : "*";
    }
}
