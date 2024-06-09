package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import java.util.Set;
import java.util.stream.Collectors;

public record AddTrackBody(Set<String> uris, int position) {
    public AddTrackBody {
        uris = uris.stream()
                .map(uri -> "spotify:track:" + uri)
                .collect(Collectors.toSet());
    }

}
