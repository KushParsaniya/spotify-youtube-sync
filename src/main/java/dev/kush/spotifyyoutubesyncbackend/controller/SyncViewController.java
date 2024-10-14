package dev.kush.spotifyyoutubesyncbackend.controller;

import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncResponseDto;
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
        SyncResponseDto syncResponseDto = null;
        if (syncDto.link() == null || syncDto.link().isBlank()) {
            syncResponseDto = syncService.syncYoutubePlayListToSpotify(syncDto.spotifyUserId(), syncDto.youtubeUserId());
            session.setAttribute("syncResponseDto", syncResponseDto);
        } else {
            syncResponseDto = syncService.syncYoutubePlayListToSpotifyByPlayListLink(syncDto.spotifyUserId(), syncDto.youtubeUserId(), syncDto.link());
            session.setAttribute("syncResponseDto", syncResponseDto);
        }
        System.out.println(syncResponseDto);
        return "redirect:/";
    }

    @PostMapping("/removeSyncResponseDto")
    public void removeSyncResponseDto(HttpSession session) {
        session.removeAttribute("syncResponseDto");
    }
}
