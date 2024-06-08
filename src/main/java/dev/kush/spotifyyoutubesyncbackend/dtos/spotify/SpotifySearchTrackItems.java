package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifySearchTrackItems(@JsonProperty("id") String trackId, String name,
                                      @JsonProperty("duration_ms") Integer duration) {
}
