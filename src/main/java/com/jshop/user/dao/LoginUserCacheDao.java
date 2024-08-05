package com.jshop.user.dao;

import com.jshop.infra.enums.CacheKey;
import lombok.RequiredArgsConstructor;
import org.hibernate.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class LoginUserCacheDao {
    private final RedisTemplate<String, Object> redisTemplate;

    public int getLoginAttemptCount(String username) {
        String key = CacheKey.LOGIN_ATTEMPTS.getKey() + username;
        return (int) Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse(0);
    }

    public void addLoginAttemptCount(String username) {
        String key = CacheKey.LOGIN_ATTEMPTS.getKey() + username;
        Long increment = Optional.ofNullable(redisTemplate.opsForValue().increment(key, 1)).orElse(0L);
        if (increment.intValue() == 1) {
            redisTemplate.expire(key, 10,  TimeUnit.MINUTES);
        }
    }

    public void deleteLoginAttemptCount(String username) {
        String key = CacheKey.LOGIN_ATTEMPTS.getKey() + username;
        redisTemplate.delete(key);
    }
}
