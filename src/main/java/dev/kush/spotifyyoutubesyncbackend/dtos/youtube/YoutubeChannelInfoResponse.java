package dev.kush.spotifyyoutubesyncbackend.dtos.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;

public record YoutubeChannelInfoResponse(@JsonProperty("id") String youtubeUserId, YoutubeChannelInfo snippet) {
}
