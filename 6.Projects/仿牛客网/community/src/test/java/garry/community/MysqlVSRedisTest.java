package garry.community;

import garry.community.service.impl.UserServiceMysqlImpl;
import garry.community.service.impl.UserServiceRedisImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/3/31 11:34
 **/
@Slf4j
public class MysqlVSRedisTest extends CommunityApplicationTests {
    @Resource
    private UserServiceMysqlImpl userServiceMysql;

    @Resource
    private UserServiceRedisImpl userServiceRedis;

    @Test
    public void MysqlTest() {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            userServiceMysql.findByUserId(176);
        }
        long end = System.currentTimeMillis();
        log.info("【Mysql用时: {}(ms)】", end - begin);
    }

    @Test
    public void RedisTest() {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            userServiceRedis.findByUserId(176);
        }
        long end = System.currentTimeMillis();
        log.info("【Redis用时: {}(ms)】", end - begin);
    }
}
