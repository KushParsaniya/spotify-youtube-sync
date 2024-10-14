package dev.kush.spotifyyoutubesyncbackend.dtos.sync;

import java.io.Serializable;

public record SyncResponseDto(boolean status, int totalSong, int syncedSong) implements Serializable {
}