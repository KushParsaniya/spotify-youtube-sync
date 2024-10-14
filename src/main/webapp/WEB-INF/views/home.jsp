<%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto" %>
<%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.youtube.YoutubeUserDto" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YouTube to Spotify Sync</title>
    <link rel="icon" href="https://th.bing.com/th/id/OIG2.z8kD.T2o.DU9xvS8SzAd?pid=ImgGn" type="image/jpeg">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .btn-youtube {
            background-color: #FF0000;
            color: white;
        }
        .btn-spotify {
            background-color: #1DB954;
            color: white;
        }
        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1050;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-5">YouTube to Spotify Sync</h1>
    <div class="row">
        <!-- YouTube Section -->
        <div class="col-md-6 mb-4">
            <div class="card h-100">
                <div class="card-body">
                    <h2 class="card-title"><i class="fab fa-youtube text-danger"></i> YouTube</h2>
                    <c:choose>
                        <c:when test="${sessionScope.isYoutubeAuth}">
                            <p><i class="fas fa-check-circle text-success"></i> Logged in as ${sessionScope.youtubeUserName}</p>
                            <form action="/youtube/logout" method="post">
                                <button type="submit" class="btn btn-outline-danger">
                                    <i class="fas fa-sign-out-alt"></i> Logout
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p>Authenticate with Google to access your YouTube playlists.</p>
                            <a href="${youtubeAuthUrl}" class="btn btn-youtube">
                                <i class="fab fa-google"></i> Login with Google (YouTube)
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <!-- Spotify Section -->
        <div class="col-md-6 mb-4">
            <div class="card h-100">
                <div class="card-body">
                    <h2 class="card-title"><i class="fab fa-spotify text-success"></i> Spotify</h2>
                    <c:choose>
                        <c:when test="${sessionScope.isSpotifyAuth}">
                            <p><i class="fas fa-check-circle text-success"></i> Logged in as ${sessionScope.spotifyUserName}</p>
                            <form action="/spotify/logout" method="post">
                                <button type="submit" class="btn btn-outline-danger">
                                    <i class="fas fa-sign-out-alt"></i> Logout
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p>Authenticate with Spotify to access your Spotify playlists.</p>
                            <a href="${spotifyAuthUrl}" class="btn btn-spotify">
                                <i class="fab fa-spotify"></i> Login with Spotify
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Sync Form -->
    <c:if test="${sessionScope.isSpotifyAuth && sessionScope.isYoutubeAuth}">
        <div class="card mt-4">
            <div class="card-body">
                <h3 class="card-title">Sync YouTube Playlist to Spotify</h3>
                <form action="/sync/" method="post" class="mt-3">
                    <div class="mb-3">
                        <label for="playlistLink" class="form-label">Enter YouTube Playlist Link:</label>
                        <input type="text" class="form-control" id="playlistLink" name="link" required>
                    </div>
                    <input type="hidden" id="spotifyUserId" name="spotifyUserId" value="${sessionScope.spotifyUserId}">
                    <input type="hidden" id="youtubeUserId" name="youtubeUserId" value="${sessionScope.youtubeUserId}">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-sync-alt"></i> Sync
                    </button>
                </form>
            </div>
        </div>
    </c:if>
</div>

<!-- Success Toast Notification -->
<c:if test="${sessionScope.syncResponseDto != null && sessionScope.syncResponseDto.status()}">
    <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <i class="fas fa-check-circle"></i> Sync Successful! Synced ${sessionScope.syncResponseDto.syncedSong()} of ${sessionScope.syncResponseDto.totalSong()} songs.
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</c:if>

<!-- Bootstrap 5 JS Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        var toastElList = [].slice.call(document.querySelectorAll('.toast'))
        var toastList = toastElList.map(function(toastEl) {
            return new bootstrap.Toast(toastEl, { autohide: true, delay: 3000 })
        });
        toastList.forEach(toast => toast.show());
        fetch('/removeSyncResponseDto', { method: 'POST' });
    });
</script>

</body>
</html>