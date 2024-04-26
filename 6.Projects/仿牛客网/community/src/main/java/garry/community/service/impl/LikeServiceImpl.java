package garry.community.service.impl;

import garry.community.service.LikeService;
import garry.community.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/23 21:56
 **/
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Service
public class LikeServiceImpl implements LikeService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void like(Integer userId, Integer entityType, Integer entityId, Integer receiverId) {
        //采用事务管理
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(receiverId);
                SetOperations opsForSet = redisOperations.opsForSet();

                //在事务外查询，避免事务内部查不到
                Boolean isMember = opsForSet.isMember(entityLikeKey, userId);

                //开启事务
                redisOperations.multi();

                //存入实体set
                if (isMember) {
                    opsForSet.remove(entityLikeKey, userId);
                } else {//否则加入集合
                    opsForSet.add(entityLikeKey, userId);
                }

                //存入用户set
                if (isMember) {
                    opsForSet.remove(userLikeKey, userId);
                } else {//否则加入集合
                    opsForSet.add(userLikeKey, userId);
                }

                return redisOperations.exec();
            }
        });
    }

    @Override
    public long findEntityLikeCount(Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        SetOperations opsForSet = redisTemplate.opsForSet();
        return opsForSet.size(entityLikeKey);
    }

    @Override
    public int findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        SetOperations opsForSet = redisTemplate.opsForSet();
        return opsForSet.isMember(entityLikeKey, userId) ? 1 : 0;
    }

    @Override
    public long findUserLikeCount(Integer userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        SetOperations opsForSet = redisTemplate.opsForSet();
        return opsForSet.size(userLikeKey);
    }
}
