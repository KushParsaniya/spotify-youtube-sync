package dev.kush.spotifyyoutubesyncbackend.services.sync.impl;

import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.AddTrackBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyCreatePlayListSuccess;
import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncResponseDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeItemsDto;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyService;
import dev.kush.spotifyyoutubesyncbackend.services.sync.SyncService;
import dev.kush.spotifyyoutubesyncbackend.services.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncServiceImpl implements SyncService {

    private final SpotifyService spotifyService;
    private final YoutubeService youtubeService;

    @Override
    public SyncResponseDto syncYoutubePlayListToSpotify(String spotifyUserId, String youtubeUserId) {
        log.info("SyncService :: syncYoutubePlayListToSpotify :: Started");
        log.info("Playlist link not found.");
        log.info("Started Syncing First Playlist.");
        YoutubeItemsDto youtubePlaylist = youtubeService.getPlaylist(youtubeUserId);

        List<YoutubeItemsDto> playlistItems = getPlaylistItems(youtubePlaylist, youtubeUserId);
        log.info("Youtube Playlist items Count : {}",playlistItems.size());

        if (playlistItems.isEmpty()) {
            return new SyncResponseDto(true, 0, 0);
        }

        SpotifyCreatePlayListSuccess spotifyPlaylist = createSpotifyPlaylist(spotifyUserId, youtubePlaylist);
        Set<String> spotifyTrackIds = getSpotifyTrackIds(spotifyUserId, playlistItems);
        log.info("Spotify Playlist Founded item count: {}", spotifyTrackIds.size());
        var status = spotifyService.addTracksToPlaylist(spotifyUserId, spotifyPlaylist, new AddTrackBody(spotifyTrackIds, 0));
        log.info("SyncService :: syncYoutubePlayListToSpotify :: Ended");
        return new SyncResponseDto(status, playlistItems.size(), spotifyTrackIds.size());
    }

    @Override
    public SyncResponseDto syncYoutubePlayListToSpotifyByPlayListLink(String spotifyUserId, String youtubeUserId, String link) {
        log.info("SyncService :: syncYoutubePlayListToSpotifyByPlayListLink :: Started");
        log.info("Playlist link found. link : {}", link);
        YoutubeItemsDto youtubePlaylist = youtubeService.getPlaylistByPlaylistLink(youtubeUserId, link);

        List<YoutubeItemsDto> playlistItems = getPlaylistItems(youtubePlaylist, youtubeUserId);
        log.info("Youtube Playlist items Count : {}",playlistItems.size());

        if (playlistItems.isEmpty()) {
            return new SyncResponseDto(true, 0, 0);
        }

        SpotifyCreatePlayListSuccess spotifyPlaylist = createSpotifyPlaylist(spotifyUserId, youtubePlaylist);
        Set<String> spotifyTrackIds = getSpotifyTrackIds(spotifyUserId, playlistItems);
        log.info("Spotify Playlist Founded item count: {}", spotifyTrackIds.size());
        var status = spotifyService.addTracksToPlaylist(spotifyUserId, spotifyPlaylist, new AddTrackBody(spotifyTrackIds, 0));
        return new SyncResponseDto(status, playlistItems.size(), spotifyTrackIds.size());
    }

    private List<YoutubeItemsDto> getPlaylistItems(YoutubeItemsDto youtubePlaylist, String youtubeUserId) {
        return youtubeService.getPlaylistItems(youtubePlaylist.id(), youtubeUserId);
    }


    private SpotifyCreatePlayListSuccess createSpotifyPlaylist(String spotifyUserId, YoutubeItemsDto youtubePlaylist) {
        return spotifyService.createPlayList(spotifyUserId, new CreatePlayListBody(
                youtubePlaylist.youtubeSnippet().title(), youtubePlaylist.youtubeSnippet().description(), false));
    }

    private Set<String> getSpotifyTrackIds(String spotifyUserId, List<YoutubeItemsDto> playlistItems) {
        List<String> youtubeSongTitle = youtubeService.getPlaylistTitles(playlistItems);
        return spotifyService.searchMultipleTracks(spotifyUserId, youtubeSongTitle);
    }
}