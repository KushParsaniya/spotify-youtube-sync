package dev.kush.spotifyyoutubesyncbackend.controller;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncDto;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyService;
import dev.kush.spotifyyoutubesyncbackend.services.sync.SyncService;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriBuilderService;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final SpotifyOAuth2Service spotifyOAuth2Service;

    private final SpotifyService spotifyService;

    private final UriBuilderService uriBuilderService;

    private final YoutubeOAuth2Service youtubeOAuth2Service;

    private final SyncService syncService;

    @GetMapping("/")
    public ModelAndView index(
            @RequestParam(value = "status", required = false) boolean status) {
        ModelAndView mv = new ModelAndView(ProjectConstants.INDEX_VIEW_NAME);

        mv.addObject("status",status);
        mv.addObject("youtubeAuthUrl", uriBuilderService.getYoutubeUri());

//        mv.addObject("isSpotifyAuth", isSpotifyAuth);
//        mv.addObject("isYoutubeAuth", isYoutubeAuth);
        mv.addObject("spotifyAuthUrl", uriBuilderService.getSpotifyUri());
        return mv;
    }


    @GetMapping("/spotify-callback")
    public RedirectView spotifyCallback(@RequestParam("code") String authCode) {
        SpotifyUserDto userDto = spotifyOAuth2Service.getAccessToken(authCode);
        RedirectView redirectView = new RedirectView("/");
        if (userDto == null) {
            redirectView.addStaticAttribute("isSpotifyAuth", false);
        } else {
            redirectView.addStaticAttribute("isSpotifyAuth", true);
            redirectView.addStaticAttribute("spotifyUser", userDto);
        }
        return redirectView;
    }

    @GetMapping("/youtube-callback")
    public RedirectView youtubeCallBack(@RequestParam("code") String authCode) {
        var userDto = youtubeOAuth2Service.getAccessToken(authCode);
        RedirectView redirectView = new RedirectView("/");
        if (userDto == null) {
            redirectView.addStaticAttribute("isYoutubeAuth", false);
        } else {
            redirectView.addStaticAttribute("isYoutubeAuth", true);
            redirectView.addStaticAttribute("youtubeUser", userDto);
        }
        return redirectView;
    }

    @PostMapping("/create-playlist")
    public String createPlaylist(@RequestParam("userId") String userId, CreatePlayListBody addPlayListBody) {
        spotifyService.createPlayList(userId, addPlayListBody);
        return "redirect:/";
    }

    @PostMapping("/sync")
    public String syncYoutubePlayListToSpotify(SyncDto syncDto) {
        boolean status = false;
        if (syncDto.link() == null || syncDto.link().isBlank()) {
            status = syncService.syncYoutubePlayListToSpotify(syncDto.spotifyUserId(), syncDto.youtubeUserId());
        } else {
            status = syncService.syncYoutubePlayListToSpotifyByPlayListLink(syncDto.spotifyUserId(), syncDto.youtubeUserId(), syncDto.link());
        }
        return "redirect:/?status=" + status;
    }
}
