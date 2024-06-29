package dev.kush.spotifyyoutubesyncbackend.services.youtube.impl;

import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeItemsDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeResponseDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeSnippet;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import dev.kush.spotifyyoutubesyncbackend.repos.UserTokenRepository;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriParamExtractor;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoutubeServiceImpl implements YoutubeService {

    private final UserTokenRepository userTokenRepository;

    private final YoutubeOAuth2Service youtubeOAuth2Service;

    private final RestTemplate restTemplate;

    private final UriParamExtractor uriParamExtractor;

    private HttpHeaders createHeaders(UserToken userToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", userToken.getTokenType() + " " + userToken.getAccessToken());
        return headers;
    }

    private UserToken getUserToken(String youtubeUserId) {

        if (youtubeUserId == null) {
            return null;
        }
        Optional<UserToken> optionalUserToken = userTokenRepository.findByYoutubeUserId(youtubeUserId);
        return optionalUserToken.orElse(null);
    }


    @Override
    public YoutubeItemsDto getPlaylist(String youtubeUserId) {
        UserToken userToken = getUserToken(youtubeUserId);

        if (userToken == null) {
            return null;
        }

        if (youtubeOAuth2Service.isTokenExpired(userToken)) {
            userToken = youtubeOAuth2Service.refreshToken(userToken);
        }

        final String url = ProjectConstants.YOUTUBE_BASE_URI + ProjectConstants.YOUTUBE_LIST_PLAYLISTS_ENDPOINT
                + "?" + ProjectConstants.YOUTUBE_PARAMETER_MINE_NAME + "=" + ProjectConstants.YOUTUBE_PARAMETER_MINE_VALUE
                + "&" + ProjectConstants.YOUTUBE_PARAMETER_PART_NAME + "=" + ProjectConstants.YOUTUBE_PART_SNIPPET_VALUE;

        ResponseEntity<YoutubeResponseDto> response = getPlaylistRestCall(url, userToken, youtubeUserId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).youtubeItemsDtos().get(0);
        }
        return null;
    }

    @Override
    public YoutubeItemsDto getPlaylistByPlaylistLink(String youtubeUserId, String link) {
        UserToken userToken = getUserToken(youtubeUserId);

        if (userToken == null) {
            return null;
        }

        if (youtubeOAuth2Service.isTokenExpired(userToken)) {
            userToken = youtubeOAuth2Service.refreshToken(userToken);
        }

        String playListId = uriParamExtractor.extractPlayListIdFromPlaylistLink(link);

        final String url = ProjectConstants.YOUTUBE_BASE_URI + ProjectConstants.YOUTUBE_LIST_PLAYLISTS_ENDPOINT
                + "?" + ProjectConstants.YOUTUBE_PARAMETER_ID_NAME + "=" + playListId
                + "&" + ProjectConstants.YOUTUBE_PARAMETER_PART_NAME + "=" + ProjectConstants.YOUTUBE_PART_SNIPPET_VALUE;

        ResponseEntity<YoutubeResponseDto> response = getPlaylistRestCall(url, userToken, youtubeUserId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).youtubeItemsDtos().get(0);
        }
        return null;
    }

    @Override
    public ResponseEntity<YoutubeResponseDto> getPlaylistRestCall(String url, UserToken userToken, String youtubeUserId) {
        HttpHeaders headers = createHeaders(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, YoutubeResponseDto.class);
    }

    @Override
    public List<YoutubeItemsDto> getPlaylistItems(String playlistId, String youtubeUserId) {

        UserToken userToken = getUserToken(youtubeUserId);

        if (userToken == null) {
            return List.of();
        }

        ResponseEntity<YoutubeResponseDto> response = getPlaylistItemsRestCall(playlistId, userToken);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).youtubeItemsDtos();
        }
        return null;
    }

    @Override
    public ResponseEntity<YoutubeResponseDto> getPlaylistItemsRestCall(String playlistId, UserToken userToken) {
        final String url = ProjectConstants.YOUTUBE_BASE_URI + ProjectConstants.YOUTUBE_LIST_PLAYLIST_ITEMS_ENDPOINT
                + "?" + ProjectConstants.YOUTUBE_PARAMETER_PLAYLIST_ID_NAME + "=" + playlistId
                + "&" + ProjectConstants.YOUTUBE_PARAMETER_PART_NAME + "=" + ProjectConstants.YOUTUBE_PART_SNIPPET_VALUE
                + "," + ProjectConstants.YOUTUBE_PART_CONTENT_DETAILS_VALUE + "," + ProjectConstants.YOUTUBE_PART_ID_VALUE;

        HttpHeaders headers = createHeaders(userToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, YoutubeResponseDto.class);
    }

    @Override
    public List<String> getPlaylistTitles(List<YoutubeItemsDto> youtubeItemsDtos) {
        return youtubeItemsDtos.stream()
                .map(YoutubeItemsDto::youtubeSnippet)
                .map(YoutubeSnippet::title)
                .toList();
    }

}
