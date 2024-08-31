package dev.kush.spotifyyoutubesyncbackend.controller;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriBuilderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UriBuilderService uriBuilderService;

    @GetMapping
    public ModelAndView index(HttpServletRequest request, HttpSession session) {
        session.removeAttribute("syncStatus");
        ModelAndView mv = new ModelAndView(ProjectConstants.INDEX_VIEW_NAME);
        mv.addObject("youtubeAuthUrl", uriBuilderService.getYoutubeUri(request));
        mv.addObject("spotifyAuthUrl", uriBuilderService.getSpotifyUri(request));
        return mv;
    }
}
