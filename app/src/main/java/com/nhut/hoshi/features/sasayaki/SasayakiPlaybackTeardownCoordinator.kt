package com.nhut.hoshi.features.sasayaki

class SasayakiPlaybackTeardownCoordinator(
    private val playbackLifecycle: SasayakiPlaybackLifecycleController,
    private val mediaSessionHandle: SasayakiMediaSessionHandleCoordinator,
    private val audioAvailability: SasayakiAudioAvailabilityState,
    private val cueDisplay: SasayakiCueDisplayCoordinator,
) {
    fun teardown(
        clearCue: Boolean,
        pausePlayback: () -> Unit,
        applyCueDisplayAction: (SasayakiCueDisplayAction) -> Unit,
    ) {
        pausePlayback()
        playbackLifecycle.releaseEngine()
        mediaSessionHandle.releaseAndClear()
        audioAvailability.markAudioUnavailable()
        if (clearCue) applyCueDisplayAction(cueDisplay.clear())
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
