package dev.kush.spotifyyoutubesyncbackend.services.uri;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UriBuilderService {

    String getSpotifyUri(HttpServletRequest request);

    String getScope(List<AllOAuth2Info> allOAuth2Infos);

    String getYoutubeUri(HttpServletRequest request);

    String getRedirectUri(HttpServletRequest request, String appName);

}
