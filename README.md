# YouTube Playlist Sync to Spotify

## Overview

This project is a Spring Boot application designed to synchronize a YouTube playlist with a Spotify playlist. It allows users to seamlessly transfer their favorite YouTube playlist tracks to Spotify, enabling easy access to their preferred music across platforms. You can try this on : [Spotify Youtube Sync](https://spotify-youtube-sync-4843bef7d4d9.herokuapp.com/)

## Features

- Fetches YouTube playlist data using the YouTube Data API.
- Searches for corresponding tracks on Spotify using the Spotify Web API.
- Adds matching tracks to a user's Spotify playlist.
- Supports authentication for both YouTube and Spotify APIs.
- Utilizes Flyway for database migration.

## Technologies Used

- Spring Boot: Provides the foundation for building the application, simplifying configuration and development.
- YouTube Data API: Used to retrieve information about YouTube playlists and their respective videos.
- Spotify Web API: Used to search for tracks on Spotify and add them to playlists.
- Flyway: Handles database schema evolution through migrations.
- Maven: Dependency management and build automation.
- OAuth 2.0: Authentication mechanism for accessing YouTube and Spotify APIs securely.
- RESTful API: Exposes endpoints for interacting with the application.

## Getting Started

1. Clone the repository:

    ```bash
    git clone https://github.com/KushParsaniya/spotify-youtube-sync.git
    ```

2. Navigate to the project directory:

    ```bash
    cd spotify-youtube-sync
    ```

3. Set up API credentials:

   - Obtain API credentials for YouTube Data API and Spotify Web API.
   - Add these credentials to the application properties or environment variables.

4. Run Flyway migrations:

    ```bash
    mvn flyway:migrate
    ```

5. Build the project:

    ```bash
    mvn clean install
    ```

6. Run the application:

    ```bash
    mvn spring-boot:run
    ```

7. Access the application at `http://localhost:8080`.

## Usage

1. Authenticate with YouTube and Spotify APIs using OAuth.
2. Enter the URL of the YouTube playlist you want to sync.
3. Choose the Spotify playlist to which you want to add the tracks.
4. Click the sync button to start the synchronization process.
5. Sit back and relax as the application fetches tracks from YouTube and adds them to your Spotify playlist.

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/my-feature`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature/my-feature`).
6. Create a new Pull Request.
