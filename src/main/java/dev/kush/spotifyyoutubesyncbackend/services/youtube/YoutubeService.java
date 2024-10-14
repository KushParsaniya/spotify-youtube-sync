package dev.kush.spotifyyoutubesyncbackend.services.youtube;

import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeItemsDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeResponseDto;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface YoutubeService {
    YoutubeItemsDto getPlaylist(String youtubeUserId);

    YoutubeItemsDto getPlaylistByPlaylistLink(String youtubeUserId, String link);

    ResponseEntity<YoutubeResponseDto> getPlaylistRestCall(String url, UserToken userToken, String youtubeUserId);

    List<YoutubeItemsDto> getPlaylistItems(String playlistId, String youtubeUserId);

    YoutubeResponseDto getPlaylistItemsRestCall(String playlistId, UserToken userToken);

    List<String> getPlaylistTitles(List<YoutubeItemsDto> youtubeItemsDtos);

}
