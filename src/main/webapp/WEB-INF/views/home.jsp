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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YouTube to Spotify Sync</title>
    <link rel="icon" href="https://th.bing.com/th/id/OIG2.z8kD.T2o.DU9xvS8SzAd?pid=ImgGn" type="image/jpeg">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <style>
        .check-mark {
            color: green;
            font-size: 24px;
        }

        .toast {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            z-index: 1050;
            min-width: 300px;
            background-color: #28a745;
            color: white;
        }

        .toast .toast-header {
            background-color: #218838;
            color: white;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- YouTube Section -->
        <div class="col-md-6">
            <h2>YouTube</h2>
            <c:choose>
                <c:when test="${sessionScope.isYoutubeAuth}">
                    <p><span class="check-mark">&#10003;</span> Logged in as ${sessionScope.youtubeUserName}</p>
                    <form action="/youtube/logout" method="post">
                        <button type="submit" class="btn btn-danger">Logout</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p>Authenticate with Google to access your YouTube playlists.</p>
                    <a href="${youtubeAuthUrl}" class="btn btn-primary">Login with Google (YouTube)</a>
                </c:otherwise>
            </c:choose>
            <br><br>
            <!-- Form for YouTube Playlist Link -->
            <c:if test="${sessionScope.isSpotifyAuth && sessionScope.isYoutubeAuth}">
                <form action="/sync/" method="post">
                    <div class="form-group">
                        <label for="playlistLink">Enter YouTube Playlist Link To Be Synced To Spotify:</label>
                        <input type="text" class="form-control" id="playlistLink" name="link">
                    </div>
                    <input type="hidden" id="spotifyUserId" name="spotifyUserId" value="${sessionScope.spotifyUserId}">
                    <input type="hidden" id="youtubeUserId" name="youtubeUserId" value="${sessionScope.youtubeUserId}">
                    <button type="submit" class="btn btn-primary">Sync</button>
                </form>
            </c:if>
        </div>
        <!-- Spotify Section -->
        <div class="col-md-6">
            <h2>Spotify</h2>
            <c:choose>
                <c:when test="${sessionScope.isSpotifyAuth}">
                    <p><span class="check-mark">&#10003;</span> Logged in as ${sessionScope.spotifyUserName}</p>
                    <form action="/spotify/logout" method="post">
                        <button type="submit" class="btn btn-danger">Logout</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p>Authenticate with Spotify to access your Spotify playlists.</p>
                    <a href="${spotifyAuthUrl}" class="btn btn-success">Login with Spotify</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Success Toast Notification -->
<c:if test="${sessionScope.syncStatus}">
    <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true" data-delay="5000">
        <div class="toast-header">
            <strong class="mr-auto">Success</strong>
            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="toast-body">
            Sync Successful!
        </div>
    </div>
</c:if>

<script>
    $(document).ready(function () {
        $('.toast').toast('show');
    });
</script>

</body>
</html>
