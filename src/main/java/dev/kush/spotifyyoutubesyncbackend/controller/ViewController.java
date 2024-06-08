package dev.kush.spotifyyoutubesyncbackend.controller;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyService;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriBuilderService;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final SpotifyOAuth2Service spotifyOAuth2Service;

    private final SpotifyService spotifyService;

    private final UriBuilderService uriBuilderService;

    private final YoutubeOAuth2Service youtubeOAuth2Service;

    @GetMapping("/")
    public ModelAndView index(
            @RequestParam(value = "isSpotifyAuth", required = false) boolean isSpotifyAuth,
            @RequestParam(value = "isYoutubeAuth", required = false) boolean isYoutubeAuth) {
        ModelAndView mv = new ModelAndView(ProjectConstants.INDEX_VIEW_NAME);

//        mv.addObject("youtubeAuthUrl", "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/youtube&include_granted_scopes=true&response_type=token&client_id=879864586117-rn7gtrpntb5i4au9llhm91a6eoc13nto.apps.googleusercontent.com&redirect_uri=http://localhost:8080/youtube-callback");
        mv.addObject("youtubeAuthUrl", uriBuilderService.getYoutubeUri());

        mv.addObject("isSpotifyAuth", isSpotifyAuth);
        mv.addObject("isYoutubeAuth", isYoutubeAuth);
        mv.addObject("spotifyAuthUrl", uriBuilderService.getSpotifyUri());
        return mv;
    }

    @GetMapping("/spotify-callback")
    public String spotifyCallback(@RequestParam("code") String authCode) {
        SpotifyUserDto userDto = spotifyOAuth2Service.getAccessToken(authCode);
        if (userDto == null) {
            return "redirect:/?isSpotifyAuth=false";
        }
        return "redirect:/?isSpotifyAuth=true";
    }

    @GetMapping("/youtube-callback")
    public String youtubeCallBack(@RequestParam("code") String authCode) {
        var userDto = youtubeOAuth2Service.getAccessToken(authCode);

        if (userDto == null) {
            return "redirect:/?isYoutubeAuth=false";
        }
        return "redirect:/?isYoutubeAuth=true";
    }

    @PostMapping("/create-playlist")
    public String createPlaylist(@RequestParam("userId") String userId, CreatePlayListBody addPlayListBody) {
        spotifyService.createPlayList(userId, addPlayListBody);
        return "redirect:/";
    }
}
