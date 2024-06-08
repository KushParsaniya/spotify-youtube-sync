<%--
  Created by IntelliJ IDEA.
  User: kushparsaniya
  Date: 5/25/24
  Time: 5:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        .split {
            width: 50%;
            position: fixed;
            z-index: 1;
            top: 0;
            overflow-x: hidden;
            padding-top: 20px;
        }

        .left {
            left: 0;
        }

        .right {
            right: 0;
        }
    </style>
</head>
<body>
<div class="split left">
    <h2>Spotify</h2>
    <% if ((Boolean) request.getAttribute("isSpotifyAuth")) { %>
    <p style="color: green">✓</p>
    <% } %>
    <button onclick="location.href='${spotifyAuthUrl}'">Login</button>
    <form action="/spotify/createPlaylist" method="post">
        <label for="spotifyPlaylistLink">Playlist Link:</label><br>
        <input type="text" id="spotifyPlaylistLink" name="spotifyPlaylistLink"><br>
        <input type="submit" value="Submit">
    </form>
    <form method="post" action="/create-playlist?userId=31muffa5emoiqtypo5o3op64qkwm">
        <label for="spotifyPlaylistName">Playlist Name:</label><br>
        <input type="text" id="spotifyPlaylistName" name="name"><br>

        <label for="spotifyPlaylistDescription">Playlist Description:</label><br>
        <input type="text" id="spotifyPlaylistDescription" name="description"><br>

        <label>Visibility:</label><br>
        <input type="radio" id="spotifyPublic" name="isPublic" value="true" checked>
        <label for="spotifyPublic">Public</label><br>
        <input type="radio" id="spotifyPrivate" name="isPublic" value="false">
        <label for="spotifyPrivate">Private</label><br>

        <br>
        <input type="submit" value="Submit">
    </form>
</div>

<div class="split right">
    <h2>YouTube</h2>
    <% if ((Boolean) request.getAttribute("isYoutubeAuth")) { %>
    <p style="color: green">✓</p>
    <% } %>
    <button onclick="location.href='${youtubeAuthUrl}'">Login</button>
    <form action="/youtube/createPlaylist" method="post">
        <label for="youtubePlaylistLink">Playlist Link:</label><br>
        <input type="text" id="youtubePlaylistLink" name="youtubePlaylistLink"><br>
        <label for="youtubePlaylistName">Playlist Name:</label><br>
        <input type="text" id="youtubePlaylistName" name="youtubePlaylistName"><br>
        <label for="youtubeVisibility">Visibility:</label><br>
        <input type="text" id="youtubeVisibility" name="youtubeVisibility"><br>
        <input type="submit" value="Submit">
    </form>
</div>
</body>
</html>