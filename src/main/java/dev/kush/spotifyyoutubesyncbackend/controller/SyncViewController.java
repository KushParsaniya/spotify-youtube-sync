package dev.kush.spotifyyoutubesyncbackend.controller;

import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncDto;
import dev.kush.spotifyyoutubesyncbackend.services.sync.SyncService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sync")
@RequiredArgsConstructor
public class SyncViewController {

    private final SyncService syncService;

    @PostMapping("/")
    public String syncYoutubePlayListToSpotify(HttpSession session, SyncDto syncDto) {
        boolean status = false;
        if (syncDto.link() == null || syncDto.link().isBlank()) {
            status = syncService.syncYoutubePlayListToSpotify(syncDto.spotifyUserId(), syncDto.youtubeUserId());
            session.setAttribute("syncStatus", status);

        } else {
            status = syncService.syncYoutubePlayListToSpotifyByPlayListLink(syncDto.spotifyUserId(), syncDto.youtubeUserId(), syncDto.link());
            session.setAttribute("syncStatus", status);
        }
        return "redirect:/";
    }
}
