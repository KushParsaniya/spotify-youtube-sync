package dev.kush.spotifyyoutubesyncbackend.services.sync;

public interface SyncService {
    boolean syncYoutubePlayListToSpotify(String spotifyUserId, String youtubeUserId);

    boolean syncYoutubePlayListToSpotifyByPlayListLink(String spotifyUserId, String youtubeUserId, String link);
}
