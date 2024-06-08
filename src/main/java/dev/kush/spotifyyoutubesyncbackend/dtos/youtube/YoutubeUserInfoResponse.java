package dev.kush.spotifyyoutubesyncbackend.dtos.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;

public record YoutubeUserInfoResponse(String id, String email) {
}
