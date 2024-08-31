package dev.kush.spotifyyoutubesyncbackend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerService {

    private final CacheManager cacheManager;

    @Scheduled(fixedRate = 3600000)
    public void clearAllCache() {
        cacheManager.getCacheNames().forEach(cacheName ->
                Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }
}
