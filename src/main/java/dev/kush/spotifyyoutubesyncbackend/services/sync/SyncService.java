package dev.kush.spotifyyoutubesyncbackend.services.sync;

import dev.kush.spotifyyoutubesyncbackend.dtos.sync.SyncResponseDto;

public interface SyncService {
    SyncResponseDto syncYoutubePlayListToSpotify(String spotifyUserId, String youtubeUserId);

    SyncResponseDto syncYoutubePlayListToSpotifyByPlayListLink(String spotifyUserId, String youtubeUserId, String link);

//    boolean syncSpotifyPlayListToYoutubeByPlayListLink(String spotifyUserId, String youtubeUserId, String link);
}
