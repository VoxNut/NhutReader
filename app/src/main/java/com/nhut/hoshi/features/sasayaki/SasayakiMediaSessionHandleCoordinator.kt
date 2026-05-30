package com.nhut.hoshi.features.sasayaki

class SasayakiMediaSessionHandleCoordinator {
    private var mediaSession: SasayakiMediaSessionHandle? = null

    fun replace(handle: SasayakiMediaSessionHandle) {
        mediaSession = handle
    }

    fun activate() {
        mediaSession?.activate()
    }

    fun update(
        isPlaying: Boolean,
        currentTimeMs: Long,
        durationMs: Long,
        rate: Float,
    ) {
        mediaSession?.update(
            isPlaying = isPlaying,
            currentTimeMs = currentTimeMs,
            durationMs = durationMs,
            rate = rate,
        )
    }

    fun releaseExisting() {
        mediaSession?.release()
    }

    fun releaseAndClear() {
        mediaSession?.release()
        mediaSession = null
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
