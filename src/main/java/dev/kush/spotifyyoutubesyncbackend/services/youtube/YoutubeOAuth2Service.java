package dev.kush.spotifyyoutubesyncbackend.services.youtube;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeAccessTokenSuccessResponse;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import org.springframework.http.ResponseEntity;

public interface YoutubeOAuth2Service {

    YoutubeUserDto getAccessToken(String authCode);

    ResponseEntity<YoutubeAccessTokenSuccessResponse> getAccessTokenRestCall(String authCode, AllOAuth2Info allOAuth2Info);

    User getUserNameFromAccessToken(YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse);


}
