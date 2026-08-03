package cn.xx.infrastructure.redis;

import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/28 20:55
 */

@Service("redissonService")
public class RedissonService implements IRedisService {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public RBitSet getBitSet(String key) {
        return redissonClient.getBitSet(key);
    }
}
