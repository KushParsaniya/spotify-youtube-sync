package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyCreatePlayListSuccess(String id, String name, @JsonProperty("public") boolean isPublic) {
}
