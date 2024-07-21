package dev.kush.spotifyyoutubesyncbackend.controller;

import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/spotify")
@RequiredArgsConstructor
public class SpotifyViewController {

    private final SpotifyOAuth2Service spotifyOAuth2Service;

    private final SpotifyService spotifyService;

    @GetMapping("/callback")
    public String spotifyCallback(HttpServletRequest request, HttpSession session, @RequestParam("code") String authCode) {
        session.removeAttribute("syncStatus");

        SpotifyUserDto userDto = spotifyOAuth2Service.getAccessToken(request, authCode);
        if (userDto == null) {
            session.setAttribute("isSpotifyAuth", false);
        } else {
            session.setAttribute("isSpotifyAuth", true);
            session.setAttribute("spotifyUserId", userDto.spotifyUserId());
            session.setAttribute("spotifyUserName", userDto.username());

        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("syncStatus");
        session.removeAttribute("isSpotifyAuth");
        session.removeAttribute("spotifyUserId");
        session.removeAttribute("spotifyUserName");
        return "redirect:/";
    }

    @PostMapping("/create-playlist")
    public String createPlaylist(@RequestParam("userId") String userId, CreatePlayListBody addPlayListBody) {
        spotifyService.createPlayList(userId, addPlayListBody);
        return "redirect:/";
    }
}
