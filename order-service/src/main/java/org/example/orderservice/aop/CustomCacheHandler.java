package org.example.orderservice.aop;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
@Slf4j
public class CustomCacheHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("handleCacheGetError cache key={}, exception={}", key, exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value) {
        log.warn("handleCachePutError cache key={}, exception={}", key, exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("handleCacheEvictError cache key={}", key);

    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("handleCacheClearError cache cache key={}", cache.getName());
    }
}
