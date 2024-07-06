package dev.kush.spotifyyoutubesyncbackend.services.uri.impl;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.entities.Scope;
import dev.kush.spotifyyoutubesyncbackend.services.oauth2.OAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriBuilderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UriBuilderServiceImpl implements UriBuilderService {

    private final OAuth2Service oAuth2Service;


    @Override
    public String getSpotifyUri() {
        var allOAuth2Infos = oAuth2Service.getAllInfoFromAppName(ProjectConstants.SPOTIFY_APP_NAME);

        return allOAuth2Infos.getFirst().oAuth2Apps().getAuthTokenUrl() + "?"
                + ProjectConstants.SPOTIFY_PARAMETER_RESPONSE_TYPE_NAME
                + "=" + ProjectConstants.SPOTIFY_RESPONSE_TYPE_VALUE + "&"
                + ProjectConstants.SPOTIFY_PARAMETER_CLIENT_ID_NAME
                + "=" + allOAuth2Infos.getFirst().client().getClientId() + "&"
                + ProjectConstants.SPOTIFY_PARAMETER_REDIRECT_URI_NAME +
                "=" + allOAuth2Infos.getFirst().redirectUri().getRedirectUri() + "&"
                + ProjectConstants.SPOTIFY_PARAMETER_SCOPE_NAME + "="
                + getScope(allOAuth2Infos);
    }

    @Override
    public String getScope(List<AllOAuth2Info> allOAuth2Infos) {
        return allOAuth2Infos.stream()
                .map(AllOAuth2Info::scope)
                .map(Scope::getScopeName)
                .distinct()
                .collect(Collectors.joining("%20"));
    }

    @Override
    public String getYoutubeUri() {
        var allOAuth2Infos = oAuth2Service.getAllInfoFromAppName(ProjectConstants.YOUTUBE_APP_NAME);

        return allOAuth2Infos.get(0).oAuth2Apps().getAuthTokenUrl() + "?"
                + ProjectConstants.YOUTUBE_PARAMETER_SCOPE_NAME
                + "=" + getScope(allOAuth2Infos) + "&"
                + ProjectConstants.YOUTUBE_PARAMETER_RESPONSE_TYPE_NAME
                + "=" + ProjectConstants.YOUTUBE_RESPONSE_TYPE_VALUE + "&"
                + ProjectConstants.YOUTUBE_PARAMETER_CLIENT_ID_NAME
                + "=" + allOAuth2Infos.get(0).client().getClientId() + "&"
                + ProjectConstants.YOUTUBE_PARAMETER_REDIRECT_URI_NAME
                + "=" + allOAuth2Infos.get(0).redirectUri().getRedirectUri() + "&"
                + ProjectConstants.YOUTUBE_PARAMETER_ACCESS_TYPE_NAME
                + "=" + ProjectConstants.YOUTUBE_ACCESS_TYPE_VALUE;
    }

}
