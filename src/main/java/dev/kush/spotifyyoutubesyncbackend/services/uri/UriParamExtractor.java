package dev.kush.spotifyyoutubesyncbackend.services.uri;

import java.net.URISyntaxException;

public interface UriParamExtractor {
    String extractPlayListIdFromPlaylistLink(String link);
}
