package dev.kush.spotifyyoutubesyncbackend.services.youtube.impl;

import dev.kush.spotifyyoutubesyncbackend.constant.DateUtils;
import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeAccessTokenSuccessResponse;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserInfoResponse;
import dev.kush.spotifyyoutubesyncbackend.entities.OAuth2Apps;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import dev.kush.spotifyyoutubesyncbackend.mapper.UserMapper;
import dev.kush.spotifyyoutubesyncbackend.repos.UserRepository;
import dev.kush.spotifyyoutubesyncbackend.repos.UserTokenRepository;
import dev.kush.spotifyyoutubesyncbackend.services.oauth2.OAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoutubeOAuth2ServiceImpl implements YoutubeOAuth2Service {

    private final OAuth2Service oAuth2Service;

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private final UserMapper userMapper;

    @Override
    public YoutubeUserDto getAccessToken(String authCode) {
        var allOAuth2Info = oAuth2Service.getAllInfoFromAppName(ProjectConstants.YOUTUBE_APP_NAME).getFirst();

        if (allOAuth2Info == null) {
            // TODO: Handler Error
            throw new RuntimeException("YoutubeOAuth2ServiceImpl :: getAccessToken --> allOAuth2Info is null");
        }

        ResponseEntity<YoutubeAccessTokenSuccessResponse> responseEntity = getAccessTokenRestCall(authCode, allOAuth2Info);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse =
                    Objects.requireNonNull(responseEntity.getBody());

            User user = getUserNameFromAccessToken(Objects.requireNonNull(responseEntity.getBody()));

            if (user == null) {
                throw new RuntimeException("YoutubeOAuth2ServiceImpl :: getAccessToken --> user is null");
            }

            UserToken userToken = saveAccessTokenOfUser(youtubeAccessTokenSuccessResponse,
                    allOAuth2Info.oAuth2Apps(), user);
            return userMapper.userToYoutubeUserDto(user);
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
        String uri = ProjectConstants.GOOGLE_BASE_URI + ProjectConstants.GOOGLE_GET_USER_INFO_ENDPOINT;

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", youtubeAccessTokenSuccessResponse.tokenType()
                + " " + youtubeAccessTokenSuccessResponse.accessToken());


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<YoutubeUserInfoResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                request, YoutubeUserInfoResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return saveUserName(Objects.requireNonNull(responseEntity.getBody()), youtubeAccessTokenSuccessResponse);
        }
        return null;
    }

    @Override
    public User saveUserName(YoutubeUserInfoResponse youtubeUserInfoResponse, YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse) {
        Optional<User> optionalUser = userRepository.findByUsernameAndYoutubeUserId(youtubeUserInfoResponse.email(),
                youtubeUserInfoResponse.id());

        User user = null;
        // if user doesn't exist in DB than create one
        if (optionalUser.isEmpty()) {
            AllOAuth2Info allOAuth2Info = getAllOAuth2Info();
            user = new User();
            user.setUsername(youtubeUserInfoResponse.email());
            user.setYoutubeUserId(youtubeUserInfoResponse.id());
            user.setRole(ProjectConstants.USER_ROLE);
            user.setCreatedAt(DateUtils.getCurrentDateTime());
            user.setOAuth2Apps(allOAuth2Info.oAuth2Apps());
        } else {
            user = optionalUser.get();
            user.setYoutubeUserId(youtubeUserInfoResponse.id());
            user.setUsername(youtubeUserInfoResponse.email());
        }

        // update other user info.
        return userRepository.save(user);
    }

    @Override
    public UserToken saveAccessTokenOfUser(YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse, OAuth2Apps oAuth2Apps, User user) {
        Optional<UserToken> optionalUserToken = userTokenRepository.findByUserId(user.getUserId());

        UserToken userToken = null;
        if (optionalUserToken.isEmpty()) {
            userToken = new UserToken(
                    youtubeAccessTokenSuccessResponse.accessToken(),
                    youtubeAccessTokenSuccessResponse.refreshToken(),
                    youtubeAccessTokenSuccessResponse.tokenType(),
                    DateUtils.getCurrentDateTime(),
                    DateUtils.getCurrentDateTime().plusSeconds(youtubeAccessTokenSuccessResponse.expiresIn()),
                    user,
                    oAuth2Apps
            );
        } else {
            userToken = optionalUserToken.get();
            userToken.setUser(user);
            userToken.setAccessToken(youtubeAccessTokenSuccessResponse.accessToken());
            userToken.setRefreshToken(youtubeAccessTokenSuccessResponse.refreshToken());
            userToken.setTokenType(youtubeAccessTokenSuccessResponse.tokenType());
            userToken.setCreatedAt(DateUtils.getCurrentDateTime());
            userToken.setExpiryAt(DateUtils.getCurrentDateTime().plusSeconds(youtubeAccessTokenSuccessResponse.expiresIn()));
        }

        user.setUserToken(List.of(userToken));
        return userTokenRepository.save(userToken);
    }

    @Override
    public boolean isTokenExpired(UserToken userToken) {
        return userToken.getExpiryAt().isAfter(DateUtils.getCurrentDateTime());
    }

    @Override
    public UserToken refreshToken(UserToken userToken) {
        AllOAuth2Info allOAuth2Info = getAllOAuth2Info();

        if (allOAuth2Info == null) {
            // TODO: handle error
            throw new RuntimeException("YoutubeOAuth2Service :: refreshToken --> allOAuth2Info is null");
        }

        ResponseEntity<YoutubeAccessTokenSuccessResponse> responseEntity = refreshTokenRestCall(userToken, allOAuth2Info);

        if (responseEntity.getStatusCode().value() == 200) {
            YoutubeAccessTokenSuccessResponse youtubeAccessTokenSuccessResponse = responseEntity.getBody();
            return saveAccessTokenOfUser(youtubeAccessTokenSuccessResponse, allOAuth2Info.oAuth2Apps(), userToken.getUser());
        }
        return null;
    }

    private AllOAuth2Info getAllOAuth2Info() {
        return oAuth2Service.getAllInfoFromAppName(ProjectConstants.YOUTUBE_APP_NAME).get(1);
    }

    @Override
    public ResponseEntity<YoutubeAccessTokenSuccessResponse> refreshTokenRestCall(UserToken userToken, AllOAuth2Info allOAuth2Info) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add(ProjectConstants.YOUTUBE_BODY_CLIENT_ID_KEY, allOAuth2Info.client().getClientId());
        body.add(ProjectConstants.YOUTUBE_BODY_REFRESH_TOKEN_KEY, userToken.getRefreshToken());
        body.add(ProjectConstants.YOUTUBE_BODY_GRANT_TYPE_KEY, allOAuth2Info.grantType().getGrantTypeName());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(allOAuth2Info.oAuth2Apps().getAccessTokenUrl(),
                request, YoutubeAccessTokenSuccessResponse.class);
    }
}
