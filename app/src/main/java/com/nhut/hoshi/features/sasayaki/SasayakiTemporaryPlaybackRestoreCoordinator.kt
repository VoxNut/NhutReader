package com.nhut.hoshi.features.sasayaki

class SasayakiTemporaryPlaybackRestoreCoordinator(
    private val playbackState: SasayakiPlaybackStateCoordinator,
    private val playbackLifecycle: SasayakiPlaybackLifecycleController,
) {
    fun restoreIfNeeded(
        updateCue: (Double) -> Unit,
        updateMediaSession: () -> Unit,
    ) {
        val returnPosition = playbackState.restoreTemporaryPlaybackPositionIfNeeded() ?: return
        playbackLifecycle.seekTo((returnPosition * 1000.0).toInt())
        updateCue(returnPosition)
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
