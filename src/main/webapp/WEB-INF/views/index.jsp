<%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto" %>
<%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto" %><%--
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
    <h2>YouTube</h2>
    <% Boolean isYoutubeAuth = (Boolean) request.getAttribute("isYoutubeAuth");
        if (isYoutubeAuth != null && isYoutubeAuth) { %>
    <p style="color: green">✓</p>
    <script>
        var youtubeUserId = '<%= ((YoutubeUserDto) request.getAttribute("youtubeUser")).youtubeUserId() %>';
        localStorage.setItem('youtubeUserId', youtubeUserId);
    </script>
    <% } %>
    <button onclick="location.href='${youtubeAuthUrl}'" >Login</button>

    <script>
        function setUserId() {
            document.getElementById('youtubeUserId').value = localStorage.getItem('youtubeUserId');
            document.getElementById('spotifyUserId').value = localStorage.getItem('spotifyUserId');
        }
    </script>
    <form action="/sync" method="post" onsubmit="setUserId()">
        <label for="youtubePlaylistLink">Playlist Link:</label><br>
        <input type="text" id="youtubePlaylistLink" name="link"><br>
        <input type="hidden" id="spotifyUserId" name="spotifyUserId">
        <input type="hidden" id="youtubeUserId" name="youtubeUserId">
        <input type="submit" value="Submit">
    </form>
</div>

<div class="split right">
    <h2>Spotify</h2>
    <% Boolean isSpotifyAuth = (Boolean) request.getAttribute("isSpotifyAuth");
       if (isSpotifyAuth != null && isSpotifyAuth) { %>
    <p style="color: green">✓</p>
    <script>
        var spotifyUserId = '<%= ((SpotifyUserDto) request.getAttribute("spotifyUser")).spotifyUserId() %>';
        localStorage.setItem('spotifyUserId', spotifyUserId);
        document.getElementById('spotifyUserId').value = localStorage.getItem('spotifyUserId');
    </script>
    <% } %>
    <button onclick="location.href='${spotifyAuthUrl}'">Login</button>
    <form action="/spotify/createPlaylist" method="post">
        <input type="submit" value="Submit">
    </form>
</div>

</body>
</html>