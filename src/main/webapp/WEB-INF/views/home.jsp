<%--
  Created by IntelliJ IDEA.
  User: kushparsaniya
  Date: 6/9/24
  Time: 1:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title><%@ page import="dev.kush.spotifyyoutubesyncbackend.dtos.spotify.SpotifyUserDto" %>
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
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Sync</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet">
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

      /* Custom CSS for animations */
      @keyframes fadeIn {
        0% {
          opacity: 0;
        }
        100% {
          opacity: 1;
        }
      }

      .fade-in {
        animation: fadeIn 1s ease-in-out forwards;
      }

      .loading-spinner {
        border: 5px solid #f3f3f3; /* Light grey */
        border-top: 5px solid #3498db; /* Blue */
        border-radius: 50%;
        width: 50px;
        height: 50px;
        animation: spin 1s linear infinite;
        margin: 20px auto; /* Center */
        display: none; /* Initially hidden */
      }

      @keyframes spin {
        0% {
          transform: rotate(0deg);
        }
        100% {
          transform: rotate(360deg);
        }
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
    <button onclick="location.href='${youtubeAuthUrl}'">Login</button>

    <!-- Loading Spinner -->
    <div class="loading-spinner" id="youtubeLoadingSpinner"></div>

    <form action="/sync" method="post" onsubmit="setUserId()">
      <label for="youtubePlaylistLink">Playlist Link:</label><br>
      <input type="text" id="youtubePlaylistLink" name="link"><br>
      <input type="hidden" id="spotifyUserId" name="spotifyUserId">
      <input type="hidden" id="youtubeUserId" name="youtubeUserId">
      <input type="submit" value="Submit">
    </form>
  </div>

  <div class="split loading-spinner">
    <% if ((Boolean) request.getAttribute("status")) { %>
    <p style="color: green">✓</p>
    <% } %>
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
    <!-- Loading Spinner -->
    <div class="loading-spinner" id="spotifyLoadingSpinner"></div>
  </div>

  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    function setUserId() {
      document.getElementById('youtubeUserId').value = localStorage.getItem('youtubeUserId');
      document.getElementById('spotifyUserId').value = localStorage.getItem('spotifyUserId');
    }
  </script>
  </body>
</html>
</title>
  </head>
  <body>

  </body>
</html>
