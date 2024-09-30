package garry.train.common.consts;

/**
 * @author Garry
 * 2024-09-09 15:33
 */

/**
 * redisKey的模板常量
 */
public class RedisConst {
    /**
     * redis 存储验证码的 key，其中 %s为 mobile (之后可以改为 member.Id，因为 mobile 容易暴露用户的手机号)
     * 举例说明: code:phone_number:17380672612
     */
    public static final String CODE_FORMAT = "code:phone_number:%s";

    /**
     * 验证码 key 的过期时间
     */
    public static final Integer CODE_EXPIRE_SECOND = 60 * 5;

    /**
     * redis 存储每日余票信息的 key
     * 举例说明: daily_ticket:2024-10-14:G000224:北京~南京
     */
    public static final String DAILY_TRAIN_TICKET_FORMAT = "daily_ticket:%s:%s:%s~%s";
}
