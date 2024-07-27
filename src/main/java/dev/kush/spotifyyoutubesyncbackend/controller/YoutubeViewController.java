package dev.kush.spotifyyoutubesyncbackend.controller;

import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeOAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YoutubeViewController {

    private final YoutubeOAuth2Service youtubeOAuth2Service;

    @GetMapping("/callback")
    public String youtubeCallBack(HttpServletRequest request, HttpSession session, @RequestParam("code") String authCode) {
        session.removeAttribute("syncStatus");

        var userDto = youtubeOAuth2Service.getAccessToken(request, authCode);
        if (userDto == null) {
            session.setAttribute("isYoutubeAuth", false);
        } else {
            session.setAttribute("isYoutubeAuth", true);
            session.setAttribute("youtubeUserId", userDto.youtubeUserId());
            session.setAttribute("youtubeUserName", userDto.username());
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("syncStatus");
        session.removeAttribute("isYoutubeAuth");
        session.removeAttribute("youtubeUserId");
        session.removeAttribute("youtubeUserName");
        return "redirect:/";
    }
}
