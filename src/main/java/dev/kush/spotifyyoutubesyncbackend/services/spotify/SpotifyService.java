package dev.kush.spotifyyoutubesyncbackend.services.spotify;



import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.AddTrackBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.CreatePlayListBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyCreatePlayListSuccess;
import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface SpotifyService {
    SpotifyCreatePlayListSuccess createPlayList(String userId, CreatePlayListBody addPlayListBody);

    ResponseEntity<SpotifyCreatePlayListSuccess> createPlayListRestCall(String userId, UserToken userToken, CreatePlayListBody createPlayListBody);

    boolean addTracksToPlaylist(UserToken userToken, SpotifyCreatePlayListSuccess spotifyCreatePlayListSuccess, AddTrackBody addTrackBody);

    String searchTrack(UserToken userToken, String trackName);

    Set<String> searchMultipleTracks(UserToken userToken, List<String> trackNames);

}
