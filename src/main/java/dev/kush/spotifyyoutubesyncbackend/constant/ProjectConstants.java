package dev.kush.spotifyyoutubesyncbackend.constant;

import java.time.LocalDateTime;

public class ProjectConstants {

    //----------------- Message Constants -----------------//
    public static final String SUCCESS_MESSAGE = "ok";
    public static final String ERROR_MESSAGE = "error";
    public static final String FAILED_GENERATE_TOKEN_MESSAGE = "Failed to generate token";
    public static final String NO_SPOTIFY_CLIENT_ACCOUNT_MESSAGE = "No Spotify client Account";

    //--------------- View Name --------------------------//
    public static final String INDEX_VIEW_NAME = "index";

    //-----------------  OAuth2 Apps -----------------//
    public static final String SPOTIFY_APP_NAME = "spotify";
    public static final String YOUTUBE_APP_NAME = "youtube";


    //----------------- Spotify URL Parameter Name-------------------//
    public static final String SPOTIFY_PARAMETER_RESPONSE_TYPE_NAME = "response_type";
    public static final String SPOTIFY_PARAMETER_REDIRECT_URI_NAME = "redirect_uri";
    public static final String SPOTIFY_PARAMETER_CLIENT_ID_NAME = "client_id";
    public static final String SPOTIFY_PARAMETER_SCOPE_NAME = "scope";

    //----------------- Spotify Parameter Values -------------------//
    public static final String SPOTIFY_RESPONSE_TYPE_VALUE="code";


    //----------------- Shopify Scopes ------------------------//
    public static final String SPOTIFY_SCOPE_USER_READ = "user-read-email";
    public static final String SHOPIFY_SCOPE_PLAYLIST_UPDATE_PRIVATE = "playlist-modify-private";
    public static final String SHOPIFY_SCOPE_PLAYLIST_UPDATE_PUBLIC = "playlist-modify-public";
    public static final String SHOPIFY_SCOPE_PLAYLIST_READ = "playlist-read-private";


    //---------------- Spotify Api EndPoints-------------------//
    public static final String SPOTIFY_BASE_URI = "https://api.spotify.com/v1";
    public static final String SPOTIFY_GET_USER_INFO_ENDPOINT = "/me";
    public static final String SPOTIFY_CREATE_PLAYLIST_ENDPOINT = "/users/%s/playlists";
    public static final String SPOTIFY_ADD_TRACKS_TO_PLAYLIST_ENDPOINT = "/playlists/%s/tracks";
    public static final String SPOTIFY_SEARCH_TRACKS_ENDPOINT = "/search";

    //--------------- Spotify Body Keys ----------------------//
    public static final String SPOTIFY_BODY_GRANT_TYPE_KEY = "grant_type";
    public static final String SPOTIFY_BODY_REFRESH_TOKEN_KEY = "refresh_token";
    public static final String SPOTIFY_BODY_CLIENT_ID_KEY = "client_id";
    public static final String SPOTIFY_BODY_REDIRECT_URI_KEY = "redirect_uri";
    public static final String SPOTIFY_BODY_CODE_KEY = "code";

    //----------------- Youtube URL Parameter Name-------------------//
    public static final String YOUTUBE_PARAMETER_RESPONSE_TYPE_NAME = "response_type";
    public static final String YOUTUBE_PARAMETER_REDIRECT_URI_NAME = "redirect_uri";
    public static final String YOUTUBE_PARAMETER_CLIENT_ID_NAME = "client_id";
    public static final String YOUTUBE_PARAMETER_SCOPE_NAME = "scope";
    public static final String YOUTUBE_PARAMETER_ACCESS_TYPE_NAME = "access_type";

    //----------------- Youtube Parameter Values -------------------//
    public static final String YOUTUBE_RESPONSE_TYPE_VALUE = "code";
    public static final String YOUTUBE_INCLUDE_GRANTED_SCOPE_VALUE = "true";
    public static final String YOUTUBE_ACCESS_TYPE_VALUE = "offline";

    //--------------- Youtube Body Keys ----------------------//
    public static final String YOUTUBE_BODY_GRANT_TYPE_KEY = "grant_type";
    public static final String YOUTUBE_BODY_CLIENT_ID_KEY = "client_id";
    public static final String YOUTUBE_BODY_CLIENT_SECRET_KEY = "client_secret";
    public static final String YOUTUBE_BODY_CODE_KEY = "code";
    public static final String YOUTUBE_BODY_REDIRECT_URI_KEY = "redirect_uri";
    public static final String YOUTUBE_BODY_REFRESH_TOKEN_KEY = "refresh_token";

    //----------------- Youtube Endpoints --------------------//
    public static final String GOOGLE_BASE_URI = "https://www.googleapis.com/oauth2/v1";
    public static final String GOOGLE_GET_USER_INFO_ENDPOINT = "/userinfo";



    //------------------ User Role --------------------------//
    public static final String USER_ROLE = "ROLE_USER";

    //----------------- Current Time --------------------------//
    public static final LocalDateTime CURRENT_TIME = LocalDateTime.now();
}
