package dev.kush.spotifyyoutubesyncbackend.services.uri;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;

import java.util.List;

public interface UriBuilderService {

    String getSpotifyUri();

    String getScope(List<AllOAuth2Info> allOAuth2Infos);

    String getYoutubeUri();

}
