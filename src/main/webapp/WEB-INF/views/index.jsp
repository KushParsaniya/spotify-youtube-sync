<%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto" %>
<%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto" %>
<%--
  Created by IntelliJ IDEA.
  User: kushparsaniya
  Date: 5/25/24
  Time: 5:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <c:if test="${sessionScope.isYoutubeAuth}">
        <h2>Spotify User Details</h2>
        <p>Username: ${sessionScope.youtubeUserName}</p>
    </c:if>
    <button onclick="location.href='${youtubeAuthUrl}'">Login</button>


    <form action="/sync" method="post">
        <label for="youtubePlaylistLink">Playlist Link:</label><br>
        <input type="text" id="youtubePlaylistLink" name="link"><br>
        <input type="hidden" id="spotifyUserId" name="spotifyUserId" value="${sessionScope.spotifyUserId}">
        <input type="hidden" id="youtubeUserId" name="youtubeUserId" value="${sessionScope.youtubeUserId}">
        <input type="submit" value="Submit">
    </form>
</div>
<div>
    <c:if test="${sessionScope.syncStatus}">
        <h2>Sync Successfully</h2>
    </c:if>
</div>


<div class="split right">
    <h2>Spotify</h2>
    <c:if test="${sessionScope.isSpotifyAuth}">
        <h2>Spotify User Details</h2>
        <p>Username: ${sessionScope.spotifyUserName}</p>
    </c:if>
    <button onclick="location.href='${spotifyAuthUrl}'">Login</button>
    <form action="/spotify/createPlaylist" method="post">
        <input type="submit" value="Submit">
    </form>
</div>

</body>
</html>