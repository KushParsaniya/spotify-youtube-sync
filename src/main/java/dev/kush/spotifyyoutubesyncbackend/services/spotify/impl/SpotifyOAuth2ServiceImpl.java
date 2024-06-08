package dev.kush.spotifyyoutubesyncbackend.services.spotify.impl;


import dev.kush.spotifyyoutubesyncbackend.constant.DateUtils;
import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyAccessTokenSuccessResponse;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserinfoResponse;
import dev.kush.spotifyyoutubesyncbackend.entities.OAuth2Apps;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import dev.kush.spotifyyoutubesyncbackend.mapper.UserMapper;
import dev.kush.spotifyyoutubesyncbackend.repos.UserRepository;
import dev.kush.spotifyyoutubesyncbackend.repos.UserTokenRepository;
import dev.kush.spotifyyoutubesyncbackend.services.oauth2.OAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyOAuth2ServiceImpl implements SpotifyOAuth2Service {

    private final RestTemplate restTemplate;

    private final UserTokenRepository userTokenRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final OAuth2Service oAuth2Service;


    @Override
    public SpotifyUserDto getAccessToken(String authCode) {

        // get Apps and client credentials From DB
        var allOAuth2Info = oAuth2Service.getAllInfoFromAppName(ProjectConstants.SPOTIFY_APP_NAME).getFirst();

        // rest call to spotify for AccessToken
        if (allOAuth2Info == null) {
            // TODO: handle error
            throw new RuntimeException("SpotifyOAuth2ServiceImpl :: getAccessToken --> allOAuth2Info is null");
        }

        ResponseEntity<SpotifyAccessTokenSuccessResponse> responseEntity = getAccessTokenRestCall(authCode, allOAuth2Info);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse =
                    Objects.requireNonNull(responseEntity.getBody());
            // now call spotify service to get user info and store it in DB
            User user = getUserNameFromAccessToken(spotifyAccessTokenSuccessResponse);

            if (user == null) {
                // TODO: handle error
                throw new RuntimeException("SpotifyOAuth2ServiceImpl :: getAccessToken --> user is null");
            }

            UserToken userToken = saveAccessTokenOfUser(spotifyAccessTokenSuccessResponse,
                    allOAuth2Info.oAuth2Apps(), user);
            return userMapper.userToSpotifyUserDto(user);
        }
        return null;
    }

    @Override
    public ResponseEntity<SpotifyAccessTokenSuccessResponse> getAccessTokenRestCall(String authCode, AllOAuth2Info allOAuth2Info) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = getStringStringMultiValueMap(authCode, allOAuth2Info);

        String authHeader = Base64.getEncoder().encodeToString((allOAuth2Info.client().getClientId()
                + ":" + allOAuth2Info.client().getClientSecret()).getBytes());

        headers.set("Authorization", "Basic " + authHeader);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(allOAuth2Info.oAuth2Apps().getAccessTokenUrl(),
                request, SpotifyAccessTokenSuccessResponse.class);

    }

    private static MultiValueMap<String, String> getStringStringMultiValueMap(String authCode, AllOAuth2Info allOAuth2Info) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add(ProjectConstants.SPOTIFY_BODY_CODE_KEY, authCode);
        body.add(ProjectConstants.SPOTIFY_BODY_REDIRECT_URI_KEY, allOAuth2Info.redirectUri().getRedirectUri());
        body.add(ProjectConstants.SPOTIFY_BODY_GRANT_TYPE_KEY, allOAuth2Info.grantType().getGrantTypeName());

        return body;
    }

    @Override
    public UserToken saveAccessTokenOfUser(SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse,
                                           OAuth2Apps oAuth2Apps, User user) {

        Optional<UserToken> optionalUserToken = userTokenRepository.findByUserId(user.getUserId());

        UserToken userToken = null;
        if (optionalUserToken.isEmpty()) {
            userToken = new UserToken(
                    spotifyAccessTokenSuccessResponse.accessToken(),
                    spotifyAccessTokenSuccessResponse.refreshToken(),
                    spotifyAccessTokenSuccessResponse.tokenType(),
                    DateUtils.getCurrentDateTime(),
                    DateUtils.getCurrentDateTime().plusSeconds(spotifyAccessTokenSuccessResponse.expiresIn()),
                    user,
                    oAuth2Apps
            );
        } else {
            userToken = optionalUserToken.get();
            userToken.setUser(user);
            userToken.setAccessToken(spotifyAccessTokenSuccessResponse.accessToken());
            userToken.setRefreshToken(spotifyAccessTokenSuccessResponse.refreshToken());
            userToken.setTokenType(spotifyAccessTokenSuccessResponse.tokenType());
            userToken.setCreatedAt(DateUtils.getCurrentDateTime());
            userToken.setExpiryAt(DateUtils.getCurrentDateTime().plusSeconds(spotifyAccessTokenSuccessResponse.expiresIn()));
        }

        user.setUserToken(List.of(userToken));
        return userTokenRepository.save(userToken);
    }


    @Override
    public User getUserNameFromAccessToken(SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse) {
        String uri = ProjectConstants.SPOTIFY_BASE_URI + ProjectConstants.SPOTIFY_GET_USER_INFO_ENDPOINT;

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", spotifyAccessTokenSuccessResponse.tokenType()
                + " " + spotifyAccessTokenSuccessResponse.accessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyUserinfoResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, request, SpotifyUserinfoResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return saveUserName(Objects.requireNonNull(response.getBody()), spotifyAccessTokenSuccessResponse);
        }
        return null;
    }

    @Override
    public User saveUserName(SpotifyUserinfoResponse spotifyUserinfoResponse, SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse) {

        Optional<User> optionalUser = userRepository.findByUsernameAndSpotifyUserId(spotifyUserinfoResponse.email(),
                spotifyUserinfoResponse.id());

        User user = null;
        // if user doesn't exist in DB than create one
        if (optionalUser.isEmpty()) {
            AllOAuth2Info allOAuth2Info = getAllOAuth2Info();
            user = new User();
            user.setUsername(spotifyUserinfoResponse.email());
            user.setSpotifyUserId(spotifyUserinfoResponse.id());
            user.setRole(ProjectConstants.USER_ROLE);
            user.setCreatedAt(DateUtils.getCurrentDateTime());
            user.setOAuth2Apps(allOAuth2Info.oAuth2Apps());
        } else {
            user = optionalUser.get();
            user.setSpotifyUserId(spotifyUserinfoResponse.id());
            user.setUsername(spotifyUserinfoResponse.email());
        }

        // update other user info.
        return userRepository.save(user);
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
            throw new RuntimeException("SpotifyOAuth2ServiceImpl :: refreshToken --> allOAuth2Info is null");
        }

        ResponseEntity<SpotifyAccessTokenSuccessResponse> responseEntity = refreshTokenRestCall(userToken, allOAuth2Info);

        if (responseEntity.getStatusCode().value() == 200) {
            SpotifyAccessTokenSuccessResponse spotifyAccessTokenSuccessResponse = responseEntity.getBody();
            return saveAccessTokenOfUser(spotifyAccessTokenSuccessResponse, allOAuth2Info.oAuth2Apps(), userToken.getUser());
        }
        return null;
    }

    private AllOAuth2Info getAllOAuth2Info() {
        return oAuth2Service.getAllInfoFromAppName(ProjectConstants.SPOTIFY_APP_NAME).get(1);
    }

    @Override
    public ResponseEntity<SpotifyAccessTokenSuccessResponse> refreshTokenRestCall(UserToken userToken, AllOAuth2Info allOAuth2Info) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add(ProjectConstants.SPOTIFY_BODY_CLIENT_ID_KEY, allOAuth2Info.client().getClientId());
        body.add(ProjectConstants.SPOTIFY_BODY_REFRESH_TOKEN_KEY, userToken.getRefreshToken());
        body.add(ProjectConstants.SPOTIFY_BODY_GRANT_TYPE_KEY, allOAuth2Info.grantType().getGrantTypeName());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(allOAuth2Info.oAuth2Apps().getAccessTokenUrl(),
                request, SpotifyAccessTokenSuccessResponse.class);
    }
}
