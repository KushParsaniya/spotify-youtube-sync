package dev.kush.spotifyyoutubesyncbackend.services.youtube.impl;

import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeAccessTokenSuccessResponse;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import dev.kush.spotifyyoutubesyncbackend.services.oauth2.OAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class YoutubeOAuth2ServiceImpl implements YoutubeOAuth2Service {

    private final OAuth2Service oAuth2Service;

    private final RestTemplate restTemplate;

    @Override
    public YoutubeUserDto getAccessToken(String authCode) {
        var allOAuth2Info = oAuth2Service.getAllInfoFromAppName(ProjectConstants.YOUTUBE_APP_NAME).getFirst();

        if (allOAuth2Info == null) {
            // TODO: Handler Error
            throw new RuntimeException("YoutubeOAuth2ServiceImpl :: getAccessToken --> allOAuth2Info is null");
        }

        ResponseEntity<YoutubeAccessTokenSuccessResponse> responseEntity = getAccessTokenRestCall(authCode, allOAuth2Info);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println(responseEntity.getBody());
            return new YoutubeUserDto("abc","def","USER");
        }
        return null;

    }

    @Override
    public ResponseEntity<YoutubeAccessTokenSuccessResponse> getAccessTokenRestCall(String authCode, AllOAuth2Info allOAuth2Info) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        final MultiValueMap<String, String> body = getStringStringMultiValueMap(authCode, allOAuth2Info);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(allOAuth2Info.oAuth2Apps().getAccessTokenUrl(),
                request, YoutubeAccessTokenSuccessResponse.class);
    }

    private static MultiValueMap<String, String> getStringStringMultiValueMap(String authCode, AllOAuth2Info allOAuth2Info) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add(ProjectConstants.YOUTUBE_BODY_CODE_KEY, authCode);
        body.add(ProjectConstants.YOUTUBE_BODY_CLIENT_ID_KEY, allOAuth2Info.client().getClientId());
        body.add(ProjectConstants.YOUTUBE_BODY_CLIENT_SECRET_KEY, allOAuth2Info.client().getClientSecret());
        body.add(ProjectConstants.YOUTUBE_BODY_REDIRECT_URI_KEY, allOAuth2Info.redirectUri().getRedirectUri());
        body.add(ProjectConstants.YOUTUBE_BODY_GRANT_TYPE_KEY, allOAuth2Info.grantType().getGrantTypeName());
        return body;
    }

    @Override
    public User getUserNameFromAccessToken(YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse) {
        return null;
    }
}
