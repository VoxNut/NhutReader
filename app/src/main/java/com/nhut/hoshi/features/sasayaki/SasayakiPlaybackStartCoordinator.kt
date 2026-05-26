package com.nhut.hoshi.features.sasayaki

class SasayakiPlaybackStartCoordinator(
    private val playbackCommands: SasayakiPlaybackCommandCoordinator,
    private val cuePresentation: SasayakiCuePresentationState,
    private val mediaSessionPublishing: SasayakiMediaSessionPublishingCoordinator,
) {
    fun start(
        rate: Float,
        currentTime: () -> Double,
        updateMediaSession: () -> Unit,
        redisplayCue: (Double) -> Unit,
    ) {
        playbackCommands.start(
            rate = rate,
            markPlayedOnce = cuePresentation::markPlayedOnce,
            afterMarkedPlaying = {
                updateMediaSession()
                mediaSessionPublishing.activate()
                redisplayCue(currentTime())
            },
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
