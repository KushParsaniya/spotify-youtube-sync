package dev.kush.spotifyyoutubesyncbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpotifyYoutubeSyncBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyYoutubeSyncBackendApplication.class, args);
    }

}
