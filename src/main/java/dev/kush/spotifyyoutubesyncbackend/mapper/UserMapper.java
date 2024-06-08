package dev.kush.spotifyyoutubesyncbackend.mapper;


import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public SpotifyUserDto userToSpotifyUserDto(User user) {
        return new SpotifyUserDto(user.getUsername(), user.getSpotifyUserId(), user.getRole());
    }
}