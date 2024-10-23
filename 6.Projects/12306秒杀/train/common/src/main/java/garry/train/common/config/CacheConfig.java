package garry.train.common.config;

import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * 2024-10-23 16:05
 */
@Configuration
@EnableCaching // 允许使用缓存
public class CacheConfig {

    private final String cacheFilePath = "common/src/main/java/garry/train/common/config/cache/";

    private final String prefix = "train_cache:";

    private final Duration defaultExpireDuration = Duration.ofMinutes(10L); // 默认过期时间 10 分钟

    /**
     * 无过期时间的缓存配置
     */
    private final RedisCacheConfiguration NO_EXPIRE_CACHE_CONFIG;

    /**
     * 默认过期时间的缓存配置
     */
    private final RedisCacheConfiguration DEFAULT_CACHE_CONFIG;

    {
        NO_EXPIRE_CACHE_CONFIG = RedisCacheConfiguration
                .defaultCacheConfig()
                .prefixCacheNameWith(prefix)
                .entryTtl(Duration.ZERO) // 设置TTL为0表示永不过期
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        DEFAULT_CACHE_CONFIG = RedisCacheConfiguration
                .defaultCacheConfig()
                .prefixCacheNameWith(prefix)
                .entryTtl(defaultExpireDuration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 读取所有不设置缓存过期时间的缓存名
        String noExpireCacheNameFileName = cacheFilePath + "no_expire_cache_name.txt";
        ArrayList<String> noExpireCacheNames = new ArrayList<>();
        try {
            List<String> lines = Files.lines(Paths.get(noExpireCacheNameFileName))
                    .map(String::trim) // 去掉每行的首尾空格
                    .filter(line -> !line.isEmpty()) // 过滤空行
                    .toList();
            noExpireCacheNames.addAll(lines);
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.NO_EXPIRE_CACHE_NAME_READ_FAILED);
        }

        // 创建配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        for (String noExpireCacheName : noExpireCacheNames) {
            cacheConfigurations.put(noExpireCacheName, NO_EXPIRE_CACHE_CONFIG);
        }

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(DEFAULT_CACHE_CONFIG) // 其它所有缓存使用默认配置
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
