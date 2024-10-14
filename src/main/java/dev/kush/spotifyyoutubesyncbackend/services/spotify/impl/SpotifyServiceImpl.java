package dev.kush.spotifyyoutubesyncbackend.services.spotify.impl;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.AddTrackBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyCreatePlayListSuccess;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifySearchTrackBodySuccess;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import dev.kush.spotifyyoutubesyncbackend.repos.UserTokenRepository;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyServiceImpl implements SpotifyService {

    private final RestTemplate restTemplate;

    private final UserTokenRepository userTokenRepository;

    private final SpotifyOAuth2Service spotifyOAuth2Service;


    private HttpHeaders createHeaders(UserToken userToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userToken.getTokenType() + " " + userToken.getAccessToken());
        return headers;
    }

    private UserToken getUserToken(String spotifyUserId) {
        if (spotifyUserId == null) {
            return null;
        }

        Optional<UserToken> optionalUserToken = userTokenRepository.findBySpotifyUserId(spotifyUserId);
        return optionalUserToken.orElse(null);
    }

    @Override
    public SpotifyCreatePlayListSuccess createPlayList(String spotifyUserId, CreatePlayListBody createPlayListBody) {
        log.info("ShopifyService :: createPlayList :: Started");
        var userToken = getUserToken(spotifyUserId);

        if (userToken == null) {
            return null;
        }

        if (spotifyOAuth2Service.isTokenExpired(userToken)) {
            userToken = spotifyOAuth2Service.refreshToken(userToken);
        }


        ResponseEntity<SpotifyCreatePlayListSuccess> response = createPlayListRestCall(spotifyUserId, userToken, createPlayListBody);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Spotify playlist created successfully");
            return response.getBody();
        }
        return null;
    }

    @Override
    public ResponseEntity<SpotifyCreatePlayListSuccess> createPlayListRestCall(String spotifyUserId, UserToken userToken, CreatePlayListBody createPlayListBody) {
        String uri = String.format(ProjectConstants.SPOTIFY_BASE_URI +
                ProjectConstants.SPOTIFY_CREATE_PLAYLIST_ENDPOINT, spotifyUserId);

        HttpHeaders headers = createHeaders(userToken);

        HttpEntity<CreatePlayListBody> request = new HttpEntity<>(createPlayListBody, headers);

        return restTemplate.postForEntity(uri, request, SpotifyCreatePlayListSuccess.class);
    }

    @Override
    public boolean addTracksToPlaylist(String spotifyUserId, SpotifyCreatePlayListSuccess spotifyCreatePlayListSuccess, AddTrackBody addTrackBody) {
        log.info("SpotifyService :: addTracksToPlaylist :: Started");
        var userToken = getUserToken(spotifyUserId);

        if (userToken == null) {
            return false;
        }

        String uri = String.format(ProjectConstants.SPOTIFY_BASE_URI +
                ProjectConstants.SPOTIFY_ADD_TRACKS_TO_PLAYLIST_ENDPOINT, spotifyCreatePlayListSuccess.id());

        HttpHeaders headers = createHeaders(userToken);

        HttpEntity<AddTrackBody> request = new HttpEntity<>(addTrackBody, headers);

        var response = restTemplate.postForEntity(uri, request, String.class);
        log.info("addTracksToPlaylist response : {}", response.getBody());
        log.info("SpotifyService :: addTracksToPlaylist :: Ended");
        return response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public String searchTrack(UserToken userToken, String trackName) {

        String uri = ProjectConstants.SPOTIFY_BASE_URI + ProjectConstants.SPOTIFY_SEARCH_TRACKS_ENDPOINT
                + "?q=" + trackName
                + "&type=track";

        HttpHeaders headers = createHeaders(userToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        var response = restTemplate.exchange(uri, HttpMethod.GET, request, SpotifySearchTrackBodySuccess.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).tracks().spotifySearchTrackItems().getFirst().trackId();
        }
        return null;
    }

    @Override
    public Set<String> searchMultipleTracks(String spotifyUserId, List<String> trackNames) {

        var userToken = getUserToken(spotifyUserId);

        if (spotifyUserId == null) {
            return Set.of();
        }

        Set<String> trackIds = new HashSet<>();

        for (String trackName : trackNames) {
            final String searchedTrackResult = searchTrack(userToken, trackName);
            if (searchedTrackResult != null) {
                trackIds.add(searchedTrackResult);
            }
        }

        return trackIds;
    }
}
