package garry.community.service.impl;

import garry.community.service.FollowService;
import garry.community.utils.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Garry
 * ---------2024/3/24 16:13
 **/
@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public void follow(Integer userId, Integer entityType, Integer entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                ZSetOperations opsForZSet = redisOperations.opsForZSet();

                //开启事务
                redisOperations.multi();

                opsForZSet.add(followeeKey, entityId, System.currentTimeMillis());
                opsForZSet.add(followerKey, userId, System.currentTimeMillis());

                return redisOperations.exec();
            }
        });
    }

    @Override
    public void unfollow(Integer userId, Integer entityType, Integer entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                ZSetOperations opsForZSet = redisOperations.opsForZSet();

                //开启事务
                redisOperations.multi();

                opsForZSet.remove(followeeKey, entityId);
                opsForZSet.remove(followerKey, userId);

                return redisOperations.exec();
            }
        });
    }

    @Override
    public long findFollowerCount(Integer entityType, Integer entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().size(followerKey);
    }

    @Override
    public long findFolloweeCount(Integer userId, Integer entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().size(followeeKey);
    }

    @Override
    public Set<Integer> findFollowerSet(Integer entityType, Integer entityId, Integer pageNum, Integer pageSize) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.reverseRange(followerKey, (pageNum - 1) * pageSize, pageNum * pageSize - 1);
    }

    @Override
    public Set<Integer> findFolloweeSet(Integer userId, Integer entityType, Integer pageNum, Integer pageSize) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.reverseRange(followeeKey, (pageNum - 1) * pageSize, pageNum * pageSize - 1);
    }

    @Override
    public int findFollowStatus(Integer userId, Integer entityType, Integer entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.score(followerKey, userId) == null ? 0 : 1;
    }

    @Override
    public long findFollowDateTime(Integer userId, Integer entityType, Integer entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        Double followDateTime = opsForZSet.score(followerKey, userId);
        return followDateTime == null ? 0 : followDateTime.longValue();
    }
}
