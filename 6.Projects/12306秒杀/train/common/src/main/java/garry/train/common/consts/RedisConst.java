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

    /**
     * redis 作分布式锁解决超卖问题的 key
     * 举例说明: daily_ticket_distributed_lock:2024-10-30:G000224
     */
    public static final String DAILY_TICKET_DISTRIBUTED_LOCK_FORMAT = "daily_ticket_distributed_lock:%s:%s";

    /**
     * skToken 的分布式锁，解决机器人刷票问题
     * 举例说明: sk_token_distributed_lock:2024-10-30:G000224:1833041335083470848
     */
    public static final String SK_TOKEN_DISTRIBUTED_LOCK_FORMAT = "sk_token_distributed_lock:%s:%s:%s";

    /**
     * 分布式锁自动释放时间
     */
    public static final Long SK_TOKEN_DISTRIBUTED_LOCK_EXPIRE_SECOND = 5L;

    /**
     * skToken 的 redis 缓存，以防大量购票请求查 skToken，搞崩数据库
     * 举例说明: sk_token_distributed_lock:2024-10-30:G000224
     */
    public static final String SK_TOKEN_FORMAT = "sk_token_distributed_lock:%s:%s";
}
