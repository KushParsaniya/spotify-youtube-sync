package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyAccessTokenSuccessResponse(
        @JsonProperty("access_token") String accessToken, @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn, @JsonProperty("refresh_token") String refreshToken
) {
}
