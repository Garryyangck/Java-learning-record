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
     * redis存储验证码的 key，其中 %s为 mobile(之后可以改为 member.Id，因为 mobile容易暴露用户的手机号)
     */
    public static final String CODE_FORMAT = "code:%s";

    /**
     * 验证码 key的过期时间
     */
    public static final Integer CODE_EXPIRE_SECOND = 60;
}
