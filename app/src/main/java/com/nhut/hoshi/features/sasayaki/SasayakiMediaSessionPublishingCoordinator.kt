package com.nhut.hoshi.features.sasayaki

class SasayakiMediaSessionPublishingCoordinator(
    private val mediaSessionHandle: SasayakiMediaSessionHandleCoordinator,
) {
    fun activate() {
        mediaSessionHandle.activate()
    }

    fun update(
        isPlaying: Boolean,
        currentTime: Double,
        duration: Double,
        rate: Float,
    ) {
        mediaSessionHandle.update(
            isPlaying = isPlaying,
            currentTimeMs = (currentTime * 1000.0).toLong(),
            durationMs = (duration * 1000.0).toLong(),
            rate = rate,
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
