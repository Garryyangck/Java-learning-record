package garry.community.consts;

/**
 * @author Garry
 * ---------2024/3/16 14:21
 **/
public class CommunityConst {
    //分页导航栏，每一边的长度
    public static final Integer NAVIGATE_PAGE_WIDTH = 3;

    //Salt的长度
    public static final Integer SALT_LENGTH = 5;

    //用户激活码长度
    public static final Integer ACTIVATION_CODE_LENGTH = 20;

    //用户头像编号的上限
    public static final Integer USER_HEADER_URL_BOUND = 1000;

    //用户头像URL模板
    public static final String USER_HEADER_URL_TEMPLATE = "https://images.nowcoder.com/head/%st.png";

    //(operator-result)自动跳转时间
    public static final Integer AUTO_REDIRECTION_TIME = 10;

    //用户密码最小长度
    public static final Integer MIN_PASSWORD_LENGTH = 5;

    //登录凭证长度
    public static final Integer LOGIN_TICKET_LENGTH = 20;

    //不记住我时，登录凭证有效时间
    public static final Long DEFAULT_EXPIRED_SECONDS = 1800L;

    //记住我时，登录凭证有效时间
    public static final Long REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 7L;

    //LoginTicket Cookie的名字
    public static final String LOGIN_TICKET_COOKIE_NAME = "ticket";

    //随机文件名的长度
    public static final Integer RANDOM_FILE_NAME_LENGTH = 20;

    //敏感词的替换字符
    public static final String SENSITIVE_WORDS_REPLACEMENT = "***";

    //某个实体的赞，在Redis中的Key的模板 like:entity:1:175 -> set(userId)
    public static final String ENTITY_LIKE_KEY_TEMPLATE = "like:entity:%s:%s";

    //某个用户的赞，在Redis中的Key的模板 like:user:176 -> set(userId)
    public static final String USER_LIKE_KEY_TEMPLATE = "like:user:%s";

    //某个实体的粉丝zset，在Redis中的Key的模板 follower:entityType:entityId -> zset(userId, now)
    public static final String FOLLOWER_KEY_TEMPLATE = "follower:%s:%s";

    //某个用户的关注实体zset，在Redis中的Key的模板 followee:userId:entityType -> zset(entityId, now)
    public static final String FOLLOWEE_KEY_TEMPLATE = "followee:%s:%s";

    //redis中存储验证码的key模板，kaptcha:owner(随机字符串)
    public static final String KAPTCHA_KEY_TEMPLATE = "kaptcha:%s";

    //凭证owner的长度
    public static final Integer OWNER_LENGTH = 20;

    //kaptchaOwner Cookie的名字
    public static final String KAPTCHA_OWNER_COOKIE_NAME = "kaptchaOwner";

    //kaptcha验证码的有效时间(秒)
    public static final Integer KAPTCHA_EFFECTIVE_TIME = 60;

    //forget的verifyCode的redis key模板 forget:owner(随机字符串)
    public static final String FORGET_KEY_TEMPLATE = "forget:%s";

    //forgetVerifyCode Cookie的名字
    public static final String FORGET_OWNER_COOKIE_NAME = "forgetOwner";

    //找回密码验证码的长度
    public static final Integer VERIFY_CODE_LENGTH = 6;

    //找回密码验证码的有效时间(分钟)
    public static final Integer FORGET_EFFECTIVE_TIME = 5;

    //登录凭证的redis key模板 loginTicket:ticket
    public static final String LOGIN_TICKET_KEY_TEMPLATE = "loginTicket:%s";

    //获取redis中用户信息缓存的key user:176
    public static final String CACHE_KEY_TEMPLATE = "user:%s";

    //user缓存过期时间(秒)
    public static final Long CACHE_EXPIRED_SECONDS = 3600L;

    //评论主题
    public static final String TOPIC_COMMENT = "comment";

    //点赞主题
    public static final String TOPIC_LIKE = "like";

    //关注主题
    public static final String TOPIC_FOLLOW = "follow";

    //系统消息的编号
    public static final Integer SYSTEM_MESSAGE_FROM_ID = 1;

    //redis中user visit相关数据的key uv:date
    public static final String UV_KEY_TEMPLATE = "uv:%s";

    //redis中daily active user相关数据的key dau:date
    public static final String DAU_KEY_TEMPLATE = "dau:%s";

    //从缓存中获取需要重新计算分数的帖子的key (%s可以根据业务得到不同改变，这里为score)
    public static final String POST_KEY_TEMPLATE = "post:%s";

    //刷新帖子分数的时间间隔
    public static final Long POST_REFRESH_INTERVAL = 1000 * 60 * 5L;
}
