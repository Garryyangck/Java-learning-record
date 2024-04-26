package garry.community.service.impl;

import garry.community.service.DataService;
import garry.community.utils.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/29 21:03
 **/
@Service
public class DataServiceImpl implements DataService {
    @Resource
    private RedisTemplate redisTemplate;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUVKey(format.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    @Override
    public long calculateUV(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            throw new RuntimeException("【参数不能为空】");
        }

        /*
            整理区间内的日期
         */
        //整理中间日期的key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        while (!calendar.getTime().after(toDate)) {
            String redisKey = RedisKeyUtil.getUVKey(format.format(calendar.getTime()));
            keyList.add(redisKey);
            calendar.add(Calendar.DATE, 1);
        }

        //合并这些数据
        String redisKey = RedisKeyUtil.getUVKey(format.format(fromDate), format.format(toDate));
        redisTemplate.opsForHyperLogLog().union(redisKey, keyList.toArray());

        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    @Override
    public void recordDAU(Integer userId) {
        String redisKey = RedisKeyUtil.getDAUKey(format.format(new Date()));
        /*
            以每一天为key，存入当日活跃用户
         */
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public long calculateDAU(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null) {
            throw new RuntimeException("【参数不能为空】");
        }

        /*
            整理区间内的日期
         */
        //整理中间日期的key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        while (!calendar.getTime().after(toDate)) {
            String redisKey = RedisKeyUtil.getDAUKey(format.format(calendar.getTime()));
            keyList.add(redisKey.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        //合并这些数据
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(format.format(fromDate), format.format(toDate));
                connection.bitOp(RedisStringCommands.BitOperation.OR, redisKey.getBytes(),
                        keyList.toArray(new byte[keyList.size()][]/*指定转换的泛型类型*/));
                return connection.bitCount(redisKey.getBytes());
            }
        });
    }
}
