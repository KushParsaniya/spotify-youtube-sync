package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpotifySearchTrackBodySuccess(@JsonProperty("total") String totalItems,
                                            @JsonProperty("items") List<SpotifySearchTrackItems> spotifySearchTrackItems) {
}
