package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiPlaybackData

class SasayakiAudioRestoreWorkflowCoordinator(
    private val audioRestore: SasayakiAudioRestoreController,
    private val audioRestoreCallbacks: SasayakiAudioRestoreCallbacksCoordinator,
    private val audioRestoreResult: SasayakiAudioRestoreResultCoordinator,
) {
    fun restore(
        playback: SasayakiPlaybackData,
        currentTime: () -> Double,
        releaseExistingMediaSession: () -> Unit,
        updateMediaSession: () -> Unit,
        handleSeekComplete: () -> Unit,
        startPlayback: () -> Unit,
        pausePlayback: () -> Unit,
        previousCue: () -> Unit,
        nextCue: () -> Unit,
        isPlaying: () -> Boolean,
        updateCue: (Double) -> Unit,
    ) {
        val result = runCatching {
            audioRestore.restore(
                playback = playback,
                releaseExistingMediaSession = releaseExistingMediaSession,
                callbacks = audioRestoreCallbacks.build(
                    updateMediaSession = updateMediaSession,
                    handlePrepared = { durationMs ->
                        audioRestoreResult.handlePrepared(
                            durationMs = durationMs,
                            currentTime = currentTime(),
                            updateCue = updateCue,
                            updateMediaSession = updateMediaSession,
                        )
                    },
                    handleSeekComplete = handleSeekComplete,
                    handleError = audioRestoreResult::handleFailure,
                    startPlayback = startPlayback,
                    pausePlayback = pausePlayback,
                    previousCue = previousCue,
                    nextCue = nextCue,
                    isPlaying = isPlaying,
                ),
            )
        }.onFailure(audioRestoreResult::handleFailure).getOrNull() ?: return
        audioRestoreResult.handleSuccess(
            result = result,
            currentTime = currentTime(),
            updateCue = updateCue,
            updateMediaSession = updateMediaSession,
        )
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
