package com.nhut.hoshi.features.sasayaki

class SasayakiPlaybackTickCoordinator(
    private val playbackEvents: SasayakiPlaybackEventCoordinator,
    private val cuePresentation: SasayakiCuePresentationState,
    private val getCurrentChapterIndex: () -> Int,
) {
    fun tick(
        hasAudio: Boolean,
        hasMatch: Boolean,
        delay: Double,
        pausePlayback: () -> Unit,
        updateMediaSession: () -> Unit,
        applyCueDisplayAction: (SasayakiCueDisplayAction) -> Unit,
    ) {
        playbackEvents.tick(
            hasAudio = hasAudio,
            hasMatch = hasMatch,
            delay = delay,
            currentChapterIndex = getCurrentChapterIndex(),
            autoScroll = cuePresentation.autoScroll,
            hasPlayedOnce = cuePresentation.hasPlayedOnce,
            pausePlayback = pausePlayback,
            updateMediaSession = updateMediaSession,
            applyCueDisplayAction = applyCueDisplayAction,
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
