package garry.community.service;

/**
 * @author Garry
 * ---------2024/3/23 21:56
 **/
public interface LikeService {
    /**
     * 点赞功能，将点赞的用户id存入对应实体的set中，
     * 并且将点赞者的id存入收到赞的用户的set中
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @param receiverId
     */
    void like(Integer userId, Integer entityType, Integer entityId, Integer receiverId);

    /**
     * 查询某一个实体赞的数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    long findEntityLikeCount(Integer entityType, Integer entityId);

    /**
     * 查询某人对某实体的点赞状态
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    int findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId);

    /**
     * 查询用户收到的总赞数
     *
     * @param userId
     * @return
     */
    long findUserLikeCount(Integer userId);
}
