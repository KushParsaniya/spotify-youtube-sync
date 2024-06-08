package dev.kush.spotifyyoutubesyncbackend.services.oauth2;

import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;

import java.util.List;

public interface OAuth2Service {
    List<AllOAuth2Info> getAllInfoFromAppName(String appName);
}
