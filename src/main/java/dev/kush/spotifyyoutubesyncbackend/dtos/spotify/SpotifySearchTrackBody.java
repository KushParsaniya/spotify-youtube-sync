package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpotifySearchTrackBody(@JsonProperty("total") String totalItems,
                                            @JsonProperty("items") List<SpotifySearchTrackItems> spotifySearchTrackItems) {
}
