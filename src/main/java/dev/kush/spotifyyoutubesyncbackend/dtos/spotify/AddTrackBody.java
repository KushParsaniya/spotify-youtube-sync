package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;

import java.util.Set;

public record AddTrackBody(Set<String> uris, int position) {
}
