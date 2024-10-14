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
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        youtube: '#FF0000',
                        spotify: '#1DB954',
                    },
                    keyframes: {
                        spin: {
                            '0%': { transform: 'rotate(0deg)' },
                            '100%': { transform: 'rotate(360deg)' },
                        }
                    },
                    animation: {
                        'spin-slow': 'spin 3s linear infinite',
                    }
                }
            }
        }
    </script>
    <style type="text/tailwindcss">
        @layer utilities {
            .btn {
                @apply font-bold py-2 px-4 rounded transition duration-300 ease-in-out;
            }
            .btn-youtube {
                @apply bg-youtube text-white hover:bg-red-700;
            }
            .btn-spotify {
                @apply bg-spotify text-white hover:bg-green-700;
            }
            .btn-outline {
                @apply bg-transparent text-gray-700 font-semibold py-2 px-4 border border-gray-500 rounded hover:border-transparent hover:text-white;
            }
            .card {
                @apply bg-white shadow-lg rounded-lg overflow-hidden;
            }
        }
    </style>
</head>
<body class="bg-gray-100 min-h-screen">
<div class="container mx-auto px-4 py-8">
    <h1 class="text-4xl font-bold text-center mb-8 text-gray-800">YouTube to Spotify Sync</h1>
    <div class="grid md:grid-cols-2 gap-8">
        <!-- YouTube Section -->
        <div class="card">
            <div class="p-6">
                <h2 class="text-2xl font-semibold mb-4 flex items-center">
                    <i class="fab fa-youtube text-youtube mr-2"></i> YouTube
                </h2>
                <c:choose>
                    <c:when test="${sessionScope.isYoutubeAuth}">
                        <p class="mb-4 flex items-center">
                            <i class="fas fa-check-circle text-green-500 mr-2"></i>
                            Logged in as ${sessionScope.youtubeUserName}
                        </p>
                        <form action="/youtube/logout" method="post">
                            <button type="submit" class="btn btn-outline hover:bg-red-500">
                                <i class="fas fa-sign-out-alt mr-2"></i> Logout
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="mb-4">Authenticate with Google to access your YouTube playlists.</p>
                        <a href="${youtubeAuthUrl}" class="btn btn-youtube">
                            <i class="fab fa-google mr-2"></i> Login with Google (YouTube)
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <!-- Spotify Section -->
        <div class="card">
            <div class="p-6">
                <h2 class="text-2xl font-semibold mb-4 flex items-center">
                    <i class="fab fa-spotify text-spotify mr-2"></i> Spotify
                </h2>
                <c:choose>
                    <c:when test="${sessionScope.isSpotifyAuth}">
                        <p class="mb-4 flex items-center">
                            <i class="fas fa-check-circle text-green-500 mr-2"></i>
                            Logged in as ${sessionScope.spotifyUserName}
                        </p>
                        <form action="/spotify/logout" method="post">
                            <button type="submit" class="btn btn-outline hover:bg-red-500">
                                <i class="fas fa-sign-out-alt mr-2"></i> Logout
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="mb-4">Authenticate with Spotify to access your Spotify playlists.</p>
                        <a href="${spotifyAuthUrl}" class="btn btn-spotify">
                            <i class="fab fa-spotify mr-2"></i> Login with Spotify
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Sync Form -->
    <c:if test="${sessionScope.isSpotifyAuth && sessionScope.isYoutubeAuth}">
        <div class="card mt-8">
            <div class="p-6">
                <h3 class="text-2xl font-semibold mb-4">Sync YouTube Playlist to Spotify</h3>
                <form id="syncForm" action="/sync/" method="post" class="space-y-4">
                    <div>
                        <label for="playlistLink" class="block text-sm font-medium text-gray-700 mb-1">
                            Enter YouTube Playlist Link:
                        </label>
                        <input type="text" id="playlistLink" name="link" required
                               class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                    </div>
                    <input type="hidden" id="spotifyUserId" name="spotifyUserId" value="${sessionScope.spotifyUserId}">
                    <input type="hidden" id="youtubeUserId" name="youtubeUserId" value="${sessionScope.youtubeUserId}">
                    <button type="submit" id="syncButton" class="btn bg-indigo-600 text-white hover:bg-indigo-700">
                        <i id="syncIcon" class="fas fa-sync-alt mr-2"></i> Sync
                    </button>
                </form>
            </div>
        </div>
    </c:if>
</div>

<!-- Sync Animation -->
<div id="syncAnimation" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 hidden">
    <div class="bg-white p-8 rounded-lg shadow-xl text-center">
        <div class="animate-spin-slow rounded-full h-16 w-16 border-t-4 border-b-4 border-indigo-500 mx-auto mb-4"></div>
        <p class="text-xl font-semibold">Syncing in progress...</p>
        <p class="text-gray-600 mt-2">Please wait while we sync your playlist.</p>
    </div>
</div>

<!-- Success Toast Notification -->
<c:if test="${sessionScope.syncResponseDto != null && sessionScope.syncResponseDto.status()}">
    <div id="toast" class="fixed bottom-4 right-4 bg-green-500 text-white px-6 py-4 rounded-md shadow-lg">
        <div class="flex items-center justify-between">
            <div class="flex items-center">
                <i class="fas fa-check-circle mr-2"></i>
                <p>Sync Successful! Synced ${sessionScope.syncResponseDto.syncedSong()} of ${sessionScope.syncResponseDto.totalSong()} songs.</p>
            </div>
            <button onclick="closeToast()" class="ml-4 text-white hover:text-gray-200">
                <i class="fas fa-times"></i>
            </button>
        </div>
    </div>
</c:if>

<script>
    function closeToast() {
        document.getElementById('toast').style.display = 'none';
    }

    document.addEventListener('DOMContentLoaded', (event) => {
        const syncForm = document.getElementById('syncForm');
        const syncButton = document.getElementById('syncButton');
        const syncIcon = document.getElementById('syncIcon');
        const syncAnimation = document.getElementById('syncAnimation');

        if (syncForm) {
            syncForm.addEventListener('submit', function(e) {
                syncButton.disabled = true;
                syncIcon.classList.add('animate-spin');
                syncAnimation.classList.remove('hidden');
            });
        }

        const toast = document.getElementById('toast');
        if (toast) {
            setTimeout(() => {
                toast.style.display = 'none';
            }, 5000);
        }

        // If sync is complete, hide the animation
        if (${sessionScope.syncResponseDto != null && sessionScope.syncResponseDto.status()}) {
            syncAnimation.classList.add('hidden');
            syncButton.disabled = false;
            syncIcon.classList.remove('animate-spin');
        }

        fetch('/removeSyncResponseDto', { method: 'POST' });
    });
</script>
</body>
</html>