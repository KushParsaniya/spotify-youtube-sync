package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyRefreshTokenBody(@JsonProperty("grant_type") String grantType,
                                      @JsonProperty("refresh_token") String refreshToken,
                                      @JsonProperty("client_id") String clientId) {
}
