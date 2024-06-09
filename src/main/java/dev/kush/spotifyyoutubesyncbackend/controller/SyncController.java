package dev.kush.spotifyyoutubesyncbackend.controller;

import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncDto;
import dev.kush.spotifyyoutubesyncbackend.services.sync.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping
    public boolean syncYoutubePlayListToSpotify(@RequestBody SyncDto syncDto) {
        return syncService.syncYoutubePlayListToSpotify(syncDto.spotifyUserId(), syncDto.youtubeUserId());
    }
}
