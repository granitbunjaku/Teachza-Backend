package com.website.courses.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void saveCode(String key, String code)
    {
        redisTemplate.opsForValue().set(key, code);
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
    }

    public String getCode(String key)
    {
        return redisTemplate.opsForValue().get(key);
    }
}
