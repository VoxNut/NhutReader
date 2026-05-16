package com.nhut.hoshi.features.sasayaki

class SasayakiPlaybackSettingsCoordinator(
    private val playbackPersistence: SasayakiPlaybackPersistenceState,
    private val playbackLifecycle: SasayakiPlaybackLifecycleController,
) {
    fun setDelay(
        value: Double,
        currentTime: Double,
        updateCue: (Double) -> Unit,
    ) {
        playbackPersistence.setDelay(value)
        updateCue(currentTime)
    }

    fun setRate(
        value: Float,
        updateMediaSession: () -> Unit,
    ) {
        playbackPersistence.setRate(value)
        playbackLifecycle.setRateIfPlaying(value)
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
