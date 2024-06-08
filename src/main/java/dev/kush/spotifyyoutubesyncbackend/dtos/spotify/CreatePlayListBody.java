package dev.kush.spotifyyoutubesyncbackend.dtos.spotify;


import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePlayListBody(String name, String description, @JsonProperty("public") boolean isPublic) {
}
