package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiPlaybackData

import android.content.ContentResolver
import android.net.Uri

class SasayakiAudioCommandCoordinator(
    private val audioSourceRepository: SasayakiAudioRepository,
    private val playbackPersistence: SasayakiPlaybackPersistenceState,
    private val playbackState: SasayakiPlaybackStateCoordinator,
    private val audioAvailability: SasayakiAudioAvailabilityState,
    private val contentResolver: ContentResolver,
) {
    fun importAudio(
        audioUri: Uri,
        copiedAudioFileName: String?,
        teardownPlayer: (clearCue: Boolean) -> Unit,
        restoreAudio: () -> Unit,
    ) {
        teardownPlayer(false)
        playbackPersistence.importAudio(audioUri, copiedAudioFileName)
        restoreAudio()
    }

    fun clearAudio(
        playback: SasayakiPlaybackData,
        teardownPlayer: (clearCue: Boolean) -> Unit,
    ) {
        audioSourceRepository.clearAudioSource(playback, contentResolver)
        teardownPlayer(true)
        playbackPersistence.clearAudioMetadata()
        playbackState.clearAudioState()
        audioAvailability.markAudioCleared()
    }
}

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
