package dev.kush.spotifyyoutubesyncbackend.dtos.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;

public record YoutubeItemsDto(String kind, String etag, String id, @JsonProperty("snippet") YoutubeSnippet youtubeSnippet) {
}
