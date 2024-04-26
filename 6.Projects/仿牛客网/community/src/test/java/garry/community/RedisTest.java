package garry.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/29 20:05
 **/
@Slf4j
public class RedisTest extends CommunityApplicationTests {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testHyperLogLog() {
        String redisKeyTemplate = "test:hyperloglog:%S";
        for (int i = 0; i < 100; i++) {
            redisTemplate.opsForHyperLogLog().add(String.format(redisKeyTemplate, 1), i);
        }
        for (int i = 51; i < 150; i++) {
            redisTemplate.opsForHyperLogLog().add(String.format(redisKeyTemplate, 2), i);
        }
        for (int i = 101; i < 200; i++) {
            redisTemplate.opsForHyperLogLog().add(String.format(redisKeyTemplate, 3), i);
        }
        for (int i = 0; i < 200; i++) {
            redisTemplate.opsForHyperLogLog().add(String.format(redisKeyTemplate, 4), i);
        }

        for (int i = 1; i <= 4; i++) {
            Long size = redisTemplate.opsForHyperLogLog().size(String.format(redisKeyTemplate, i));
            log.info("the size of no.{} is {}", i, size);
        }

        redisTemplate.opsForHyperLogLog().union(String.format(redisKeyTemplate, "union"),
                String.format(redisKeyTemplate, 1),
                String.format(redisKeyTemplate, 2),
                String.format(redisKeyTemplate, 3),
                String.format(redisKeyTemplate, 4));
        Long size = redisTemplate.opsForHyperLogLog().size(String.format(redisKeyTemplate, "union"));
        log.info("the size of union is {}", size);
    }

    @Test
    public void testBitMap() {
        String redisKey = "test:bitmap:1";
        redisTemplate.opsForValue().setBit(redisKey, 1, true);
        redisTemplate.opsForValue().setBit(redisKey, 2, false);
        redisTemplate.opsForValue().setBit(redisKey, 3, true);

        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());//获取true的个数
            }
        });

        log.info("execute = {}", execute);
    }

    @Test
    public void testBitMapOR() {
        String redisKey1 = "test:bitmap:1";
        redisTemplate.opsForValue().setBit(redisKey1, 1, true);
        redisTemplate.opsForValue().setBit(redisKey1, 2, true);
        redisTemplate.opsForValue().setBit(redisKey1, 3, true);

        String redisKey2 = "test:bitmap:2";
        redisTemplate.opsForValue().setBit(redisKey2, 3, true);
        redisTemplate.opsForValue().setBit(redisKey2, 4, true);
        redisTemplate.opsForValue().setBit(redisKey2, 5, true);

        String redisKey3 = "test:bitmap:3";
        redisTemplate.opsForValue().setBit(redisKey3, 5, true);
        redisTemplate.opsForValue().setBit(redisKey3, 6, true);
        redisTemplate.opsForValue().setBit(redisKey3, 7, true);

        String redisKey = "test:bitmap:OR";
        Object count = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.bitOp(RedisStringCommands.BitOperation.OR, redisKey.getBytes(),
                        redisKey1.getBytes(), redisKey2.getBytes(), redisKey3.getBytes());
                return connection.bitCount(redisKey.getBytes());
            }
        });

        log.info("count = {}", count);
    }
}
