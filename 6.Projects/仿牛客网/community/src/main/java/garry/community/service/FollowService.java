package garry.community.service;

import java.util.Set;

/**
 * @author Garry
 * ---------2024/3/24 16:13
 **/
public interface FollowService {

    /**
     * 事务管理下，将userId和entityType和entityId加入redis
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    void follow(Integer userId, Integer entityType, Integer entityId);

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    void unfollow(Integer userId, Integer entityType, Integer entityId);

    /**
     * 返回粉丝人数
     *
     * @param entityType
     * @param entityId
     * @return
     */
    long findFollowerCount(Integer entityType, Integer entityId);

    /**
     * 返回用户对某一实体的关注总数
     *
     * @param userId
     * @return
     */
    long findFolloweeCount(Integer userId, Integer entityType);

    /**
     * 返回某一实体的粉丝列表，需要按照关注时间分页
     *
     * @param entityType
     * @param entityId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Set<Integer> findFollowerSet(Integer entityType, Integer entityId, Integer pageNum, Integer pageSize);

    /**
     * 返回用户对于某一实体的关注的列表，需要分页
     *
     * @param userId
     * @param entityType
     * @param pageNum
     * @param pageSize
     * @return
     */
    Set<Integer> findFolloweeSet(Integer userId, Integer entityType, Integer pageNum, Integer pageSize);

    /**
     * 查询userId是否关注了某一实体，是则返回1，不是则返回0
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    int findFollowStatus(Integer userId, Integer entityType, Integer entityId);

    /**
     * 查询某一实体的粉丝中，id为userId的粉丝关注的时间，如果不存在该粉丝则返回0
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    long findFollowDateTime(Integer userId, Integer entityType, Integer entityId);
}
