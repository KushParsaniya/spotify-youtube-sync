package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

public record SpotifyAccessTokenBody(
        String grantType, String clientId, String clientSecret
) {
}

/*curl -X POST "https://accounts.spotify.com/api/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=client_credentials&client_id=your-client-id&client_secret=your-client-secret"
 */