package dev.kush.spotifyyoutubesyncbackend.services.spotify;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyAccessTokenSuccessResponse;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserinfoResponse;
import dev.kush.spotifyyoutubesyncbackend.entities.OAuth2Apps;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import org.springframework.http.ResponseEntity;

public interface SpotifyOAuth2Service {

    SpotifyUserDto getAccessToken(String authCode);

    ResponseEntity<SpotifyAccessTokenSuccessResponse> getAccessTokenRestCall(String authCode, AllOAuth2Info allOAuth2Info);

    UserToken saveAccessTokenOfUser(SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse,
                                    OAuth2Apps oAuth2Apps, User user);

    User getUserNameFromAccessToken(SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse);

    User saveUserName(SpotifyUserinfoResponse spotifyUserinfoResponse, SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse);

    boolean isTokenExpired(UserToken userToken);

    UserToken refreshToken(UserToken userToken);

    ResponseEntity<SpotifyAccessTokenSuccessResponse> refreshTokenRestCall(UserToken userToken, AllOAuth2Info allOAuth2Info);

}
