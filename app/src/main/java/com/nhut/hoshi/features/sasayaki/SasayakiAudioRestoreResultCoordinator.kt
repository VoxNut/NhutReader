package com.nhut.hoshi.features.sasayaki

class SasayakiAudioRestoreResultCoordinator(
    private val mediaSessionHandle: SasayakiMediaSessionHandleCoordinator,
    private val playbackState: SasayakiPlaybackStateCoordinator,
    private val audioAvailability: SasayakiAudioAvailabilityState,
) {
    fun handleFailure(error: Throwable) {
        audioAvailability.markRestoreFailed(error)
    }

    fun handleSuccess(
        result: SasayakiAudioRestoreResult,
        currentTime: Double,
        updateCue: (Double) -> Unit,
        updateMediaSession: () -> Unit,
    ) {
        mediaSessionHandle.replace(result.mediaSession)
        handlePrepared(
            durationMs = result.durationMs,
            currentTime = currentTime,
            updateCue = updateCue,
            updateMediaSession = updateMediaSession,
        )
    }

    fun handlePrepared(
        durationMs: Int,
        currentTime: Double,
        updateCue: (Double) -> Unit,
        updateMediaSession: () -> Unit,
    ) {
        playbackState.updateDuration(durationMs)
        audioAvailability.markRestoreSucceeded()
        updateCue(currentTime)
        updateMediaSession()
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
