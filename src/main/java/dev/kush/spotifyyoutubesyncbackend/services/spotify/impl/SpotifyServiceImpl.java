package dev.kush.spotifyyoutubesyncbackend.services.spotify.impl;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.AddTrackBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyCreatePlayListSuccess;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import dev.kush.spotifyyoutubesyncbackend.repos.UserTokenRepository;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpotifyServiceImpl implements SpotifyService {

    private final RestTemplate restTemplate;

    private final UserTokenRepository userTokenRepository;

    private final SpotifyOAuth2Service spotifyOAuth2Service;


    @Override
    public SpotifyCreatePlayListSuccess createPlayList(String userId, CreatePlayListBody createPlayListBody) {

        Optional<UserToken> optionalUserToken = getUserTokenBySpotifyReactAuthDto(userId);

        if (optionalUserToken.isEmpty()) {
            return null;
        }

        UserToken userToken = optionalUserToken.get();

        if (spotifyOAuth2Service.isTokenExpired(userToken)) {
            userToken = spotifyOAuth2Service.refreshToken(userToken);
        }


        ResponseEntity<SpotifyCreatePlayListSuccess> response = createPlayListRestCall(userId, userToken, createPlayListBody);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public ResponseEntity<SpotifyCreatePlayListSuccess> createPlayListRestCall(String userId, UserToken userToken, CreatePlayListBody createPlayListBody) {
        String uri = String.format(ProjectConstants.SPOTIFY_BASE_URI +
                ProjectConstants.SPOTIFY_CREATE_PLAYLIST_ENDPOINT, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userToken.getTokenType() + " " +
                userToken.getAccessToken());

        HttpEntity<CreatePlayListBody> request = new HttpEntity<>(createPlayListBody, headers);

        return restTemplate.postForEntity(uri, request, SpotifyCreatePlayListSuccess.class);
    }

    @Override
    public boolean addTracksToPlaylist(UserToken userToken, SpotifyCreatePlayListSuccess spotifyCreatePlayListSuccess, AddTrackBody addTrackBody) {
        String uri = String.format(ProjectConstants.SPOTIFY_BASE_URI +
                ProjectConstants.SPOTIFY_ADD_TRACKS_TO_PLAYLIST_ENDPOINT, spotifyCreatePlayListSuccess.id());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userToken.getTokenType() + " " +
                userToken.getAccessToken());

        HttpEntity<AddTrackBody> request = new HttpEntity<>(addTrackBody, headers);

        var response = restTemplate.postForEntity(uri, request, String.class);
        return response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public String searchTrack(UserToken userToken, String trackName) {

        String uri = ProjectConstants.SPOTIFY_BASE_URI + ProjectConstants.SPOTIFY_SEARCH_TRACKS_ENDPOINT
                + "?q=" + trackName
                + "&type=track";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userToken.getTokenType() + " " + userToken.getAccessToken());

        HttpEntity<String> request = new HttpEntity<>(headers);

        var response = restTemplate.getForEntity(uri, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public Set<String> searchMultipleTracks(UserToken userToken, List<String> trackNames) {

        Set<String> trackIds = new HashSet<>();

        for (String trackName : trackNames) {
            final String searchedTrackResult = searchTrack(userToken, trackName);
            if (searchedTrackResult != null) {
                trackIds.add(searchedTrackResult);
            }
        }

        return trackIds;
    }

    private Optional<UserToken> getUserTokenBySpotifyReactAuthDto(String userId) {
        return userTokenRepository.findBySpotifyUserNameAndUserId(userId);
    }
}
