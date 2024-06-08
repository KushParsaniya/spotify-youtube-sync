package dev.kush.spotifyyoutubesyncbackend.controller;


import dev.kush.spotifyyoutubesyncbackend.constant.ProjectConstants;
import dev.kush.spotifyyoutubesyncbackend.dtos.ResponseDto;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyGenerateAccessTokenBody;
import dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto;
import dev.kush.spotifyyoutubesyncbackend.services.oauth2.OAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.spotify.SpotifyOAuth2Service;
import dev.kush.spotifyyoutubesyncbackend.services.uri.UriBuilderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spotify")
@RequiredArgsConstructor
@CrossOrigin(value = "*", allowedHeaders = "*", exposedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class SpotifyController {

    private final SpotifyOAuth2Service spotifyOAuth2Service;

    private final UriBuilderService uriBuilderService;

    private final OAuth2Service oAuth2Service;

//    @GetMapping("/token")
//    public ResponseDto getAccessToken(@RequestBody SpotifyAccessTokenBody spotifyAccessTokenBody) {
//        return new ResponseDto(ProjectConstants.SUCCESS_MESSAGE, spotifyService.getAccessToken(spotifyAccessTokenBody), HttpStatus.OK.value());
//    } // This method is not used in the project

    @GetMapping("/url")
    public ResponseDto getASpotifyUri() {
        var allOAuth2Info = oAuth2Service.getAllInfoFromAppName(ProjectConstants.SPOTIFY_APP_NAME);

        if (allOAuth2Info.isEmpty()) {
            return new ResponseDto(ProjectConstants.ERROR_MESSAGE, ProjectConstants.NO_SPOTIFY_CLIENT_ACCOUNT_MESSAGE, HttpStatus.NOT_FOUND.value());
        }
        return new ResponseDto(ProjectConstants.SUCCESS_MESSAGE, uriBuilderService.getSpotifyUri(), HttpStatus.OK.value());
    }

    @PostMapping("/token")
    public ResponseDto generateToken(@RequestBody SpotifyGenerateAccessTokenBody spotifyGenerateAccessTokenBody) {
        SpotifyUserDto userDto = spotifyOAuth2Service.getAccessToken(spotifyGenerateAccessTokenBody.authCode());

        if (userDto == null) {
            return new ResponseDto(ProjectConstants.ERROR_MESSAGE, ProjectConstants.FAILED_GENERATE_TOKEN_MESSAGE, HttpStatus.BAD_REQUEST.value());
        }
        return new ResponseDto(ProjectConstants.SUCCESS_MESSAGE, userDto, HttpStatus.OK.value());
    }


}
