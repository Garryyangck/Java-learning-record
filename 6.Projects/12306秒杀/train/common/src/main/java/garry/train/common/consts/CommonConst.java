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

    /**
     * JWT 的有效小时数
     */
    public static final Integer JWT_EXPIRE_HOUR = 24;

    /**
     * DailyTrainJob 中生成 DAILY_TRAIN_OFFSET_DAYS 天后的车次数据
     */
    public static final Integer DAILY_TRAIN_OFFSET_DAYS = 15;

    /**
     * 导航页默认大小
     */
    public static final Integer DEFAULT_NAVIGATE_PAGES = 8;

    /**
     * 每个会员持有的乘车人上限
     */
    public static final Integer PASSENGER_LIMIT = 50;

    /**
     * 系统的 id
     */
    public static final Long SystemId = 0L;

    /**
     * SkToken 缓存更新至数据库的频率
     */
    public static final Integer DB_UPDATE_FREQUENCY = 5;

}
