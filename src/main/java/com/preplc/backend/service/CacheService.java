package com.preplc.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Fetch deserialized object from cache. Returns null if key is not found or if Redis is down.
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                return objectMapper.readValue(value, typeReference);
            }
        } catch (Exception e) {
            log.error("Error reading key '{}' from Redis cache: {}", key, e.getMessage());
        }
        return null;
    }

    /**
     * Store object serialized to JSON in cache with TTL in minutes. Logs and swallows exceptions on Redis failures.
     */
    public void set(String key, Object value, long ttlMinutes) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, Duration.ofMinutes(ttlMinutes));
        } catch (Exception e) {
            log.error("Error writing key '{}' to Redis cache: {}", key, e.getMessage());
        }
    }

    /**
     * Evict a key from cache.
     */
    public void evict(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Error evicting key '{}' from Redis cache: {}", key, e.getMessage());
        }
    }
}
