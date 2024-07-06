package dev.kush.spotifyyoutubesyncbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheManager cacheManager;

    @Scheduled(fixedRate = 60000)
    public void clearOAuth2InfoCache() {
        Objects.requireNonNull(cacheManager.getCache("oAuth2Info")).clear();
    }
}
