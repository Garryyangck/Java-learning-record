package garry.community.utils;

import garry.community.consts.CommunityConst;

/**
 * @author Garry
 * ---------2024/3/23 21:50
 **/
public class RedisKeyUtil {

    /**
     * 根据点赞的实体类型和id获取redis点赞set的key
     *
     * @param entityType
     * @param entityId
     * @return
     */
    //like:entity:entityType:entityId
    public static String getEntityLikeKey(Integer entityType, Integer entityId) {
        return String.format(CommunityConst.ENTITY_LIKE_KEY_TEMPLATE, entityType, entityId);
    }

    /**
     * 获取用户收到的赞的redis的key
     *
     * @param userId 收到赞的用户的userId
     * @return
     */
    //like:user:userId
    public static String getUserLikeKey(Integer userId) {
        return String.format(CommunityConst.USER_LIKE_KEY_TEMPLATE, userId);
    }

    /**
     * 获取某一实体粉丝ZSet的key
     *
     * @param entityType
     * @param entityId
     * @return
     */
    //follower:entityType:entityId -> zset(userId, now)
    public static String getFollowerKey(Integer entityType, Integer entityId) {
        return String.format(CommunityConst.FOLLOWER_KEY_TEMPLATE, entityType, entityId);
    }

    /**
     * 获取用户关注的实体的ZSet的key
     *
     * @param userId
     * @return
     */
    //followee:userId:entityType -> zset(entityId, now)
    public static String getFolloweeKey(Integer userId, Integer entityType) {
        return String.format(CommunityConst.FOLLOWEE_KEY_TEMPLATE, userId, entityType);
    }

    /**
     * 获取kaptcha验证码的key
     *
     * @param owner
     * @return
     */
    public static String getKaptchaKey(String owner) {
        return String.format(CommunityConst.KAPTCHA_KEY_TEMPLATE, owner);
    }

    /**
     * 获取forget验证码的key
     *
     * @param owner
     * @return
     */
    public static String getForgetKey(String owner) {
        return String.format(CommunityConst.FORGET_KEY_TEMPLATE, owner);
    }

    /**
     * loginTicket的key
     *
     * @param ticket
     * @return
     */
    public static String getLoginTicketKey(String ticket) {
        return String.format(CommunityConst.LOGIN_TICKET_KEY_TEMPLATE, ticket);
    }

    /**
     * 获取用户缓存的key
     *
     * @param userId
     * @return
     */
    public static String getCacheKey(Integer userId) {
        return String.format(CommunityConst.CACHE_KEY_TEMPLATE, userId);
    }

    /**
     * 获取单日uv的key
     *
     * @param date
     * @return
     */
    public static String getUVKey(String date) {
        return String.format(CommunityConst.UV_KEY_TEMPLATE, date);
    }

    /**
     * 获取某一区间的uv的key
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static String getUVKey(String fromDate, String toDate) {
        return String.format(CommunityConst.UV_KEY_TEMPLATE, fromDate + " to " + toDate);
    }

    /**
     * 获取单日dau的key
     *
     * @param date
     * @return
     */
    public static String getDAUKey(String date) {
        return String.format(CommunityConst.DAU_KEY_TEMPLATE, date);
    }

    /**
     * 获取某一区间的dau的key
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static String getDAUKey(String fromDate, String toDate) {
        return String.format(CommunityConst.DAU_KEY_TEMPLATE, fromDate + " to " + toDate);
    }

    /**
     * 获取需要重新计算分数的帖子的key
     *
     * @return
     */
    public static String getPostKey() {
        return String.format(CommunityConst.POST_KEY_TEMPLATE, "score");
    }
}
