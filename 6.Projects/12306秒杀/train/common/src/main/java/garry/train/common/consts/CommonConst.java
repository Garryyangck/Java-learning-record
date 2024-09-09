package garry.train.common.consts;

/**
 * @author Garry
 * 2024-09-06 21:27
 */

/**
 * 通用常量
 */
public class CommonConst {
    /**
     * LOG_ID的长度，用于 logback的配置中
     */
    public static final Integer LOG_ID_LENGTH = 18;

    /**
     * 雪花算法的工作 Id
     */
    public static final Integer WORKER_ID = 1;

    /**
     * 雪花算法的机器 Id
     */
    public static final Integer DATACENTER_ID = 1;

    /**
     * 短信验证码的长度
     */
    public static final Integer CODE_LENGTH = 4;
}
