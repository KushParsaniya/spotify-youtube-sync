package dev.kush.spotifyyoutubesyncbackend.services.youtube;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeAccessTokenSuccessResponse;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserInfoResponse;
import dev.kush.spotifyyoutubesyncbackend.entities.OAuth2Apps;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import org.springframework.http.ResponseEntity;

public interface YoutubeOAuth2Service {

    YoutubeUserDto getAccessToken(String authCode);

    ResponseEntity<YoutubeAccessTokenSuccessResponse> getAccessTokenRestCall(String authCode, AllOAuth2Info allOAuth2Info);

    User getUserNameFromAccessToken(YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse);

    User saveUserName(YoutubeUserInfoResponse youtubeUserInfoResponse, YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse);

    UserToken saveAccessTokenOfUser(YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse,
                                    OAuth2Apps oAuth2Apps, User user);

    boolean isTokenExpired(UserToken userToken);

    UserToken refreshToken(UserToken userToken);

    ResponseEntity<YoutubeAccessTokenSuccessResponse> refreshTokenRestCall(UserToken userToken, AllOAuth2Info allOAuth2Info);


}
