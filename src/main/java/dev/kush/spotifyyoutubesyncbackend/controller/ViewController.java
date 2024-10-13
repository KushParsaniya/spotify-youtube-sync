package dev.kush.spotifyyoutubesyncbackend.controller;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriBuilderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UriBuilderService uriBuilderService;

    @GetMapping
    public String index(HttpServletRequest request, HttpSession session, Model model) {
        session.removeAttribute("syncStatus");
        model.addAttribute("youtubeAuthUrl", uriBuilderService.getYoutubeUri(request));
        model.addAttribute("spotifyAuthUrl", uriBuilderService.getSpotifyUri(request));
        return ProjectConstants.INDEX_VIEW_NAME;
    }
}
