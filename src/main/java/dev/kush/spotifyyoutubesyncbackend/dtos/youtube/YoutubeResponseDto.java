package dev.kush.spotifyyoutubesyncbackend.dtos.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record YoutubeResponseDto(String kind, String etag,
                                 @JsonProperty("items") List<YoutubeItemsDto> youtubeItemsDtos, String nextPageToken, String prevPageToken) {
}
